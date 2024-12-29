<template>
  <div class="container">
    <h2 class="form-signin-heading">Registration</h2>
    <div v-if="errors.errorMessage" class="text-success" style="color: red">
      {{ errors.errorMessage }}
    </div>
    <form @submit.prevent="handleSubmit">
      <p>
        <label for="firstname" class="sr-only">First Name</label>
        <input
          type="text"
          id="firstname"
          v-model="form.firstName"
          class="form-control"
          placeholder="First Name"
          required
        />
      </p>
      <p>
        <label for="lastname" class="sr-only">Last Name</label>
        <input
          type="text"
          id="lastname"
          v-model="form.lastName"
          class="form-control"
          placeholder="Last Name"
          required
        />
      </p>
      <p>
        <label for="birthDate">Birth Date</label>
        <input
          type="date"
          id="birthDate"
          v-model="form.birthDate"
          class="form-control"
          required
        />
        <span v-if="errors.birthDate" style="color: red">{{
          errors.birthDate
        }}</span>
      </p>
      <p>
        <label for="country">Country</label>
        <select
          v-model="form.country"
          @change="fetchCities"
          class="form-control-select"
        >
          <option value="">Choose Country</option>
          <option
            v-for="country in countries"
            :key="country.id"
            :value="country.id"
          >
            {{ country.name }}
          </option>
        </select>
      </p>
      <p>
        <label for="city">City</label>
        <select v-model="form.city" class="form-control-select">
          <option value="">Choose City</option>
          <option v-for="city in cities" :key="city.id" :value="city.id">
            {{ city.name }}
          </option>
        </select>
      </p>
      <p>
        <label for="email" class="sr-only">Email</label>
        <input
          type="email"
          id="email"
          v-model="form.email"
          class="form-control"
          placeholder="Email"
          required
        />
        <span v-if="errors.email" class="error-message" style="color: red">{{
          errors.email
        }}</span>
      </p>
      <p>
        <label for="password" class="sr-only">Password</label>
        <input
          type="password"
          id="password"
          v-model="form.password"
          class="form-control"
          placeholder="Password"
          required
        />
        <span v-if="errors.password" class="error-message" style="color: red">{{
          errors.password
        }}</span>
      </p>
      <p>
        <label for="passwordConfirmation" class="sr-only"
          >Password Confirmation</label
        >
        <input
          type="password"
          id="passwordConfirmation"
          v-model="form.passwordConfirmation"
          class="form-control"
          placeholder="Password Confirmation"
          required
        />
        <span
          v-if="errors.passwordConfirmation"
          class="error-message"
          style="color: red"
          >{{ errors.passwordConfirmation }}</span
        >
      </p>
      <div class="btn-container">
        <button type="submit" class="btn">Sign up</button>
      </div>
    </form>
    <div class="bottom_btns">
      <p><a href="/login">Already have an account?</a></p>
    </div>
  </div>
</template>

<script>
  const BASE_URL = import.meta.env.VITE_BASE_URL;

  export default {
    data() {
      return {
        form: {
          firstName: '',
          lastName: '',
          birthDate: '',
          country: '',
          city: '',
          email: '',
          password: '',
          passwordConfirmation: '',
        },
        errorMessage: '',
        countries: [],
        cities: [],
        errors: {},
        isGraphQL: window.location.href.includes('graphql'),
      };
    },
    methods: {
      async fetchCountries() {
        try {
          const response = await fetch(`${BASE_URL}/auth/country-list`);
          this.countries = await response.json();
        } catch (error) {
          console.error('Error fetching countries:', error);
        }
      },
      async fetchCities() {
        if (!this.form.country) return;

        try {
          const response = await fetch(
            `${BASE_URL}/auth/${this.form.country}/city-list`
          );
          this.cities = await response.json();
        } catch (error) {
          console.error('Error fetching cities:', error);
        }
      },
      async handleSubmit() {
        this.errors = {};

        if (this.isGraphQL) {
          this.form.country = parseInt(this.form.country, 10);
          this.form.city = parseInt(this.form.city, 10);

          try {
            const response = await fetch(`${BASE_URL}/graphql`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                query: `
                    mutation($userRegisterDto: UserRegisterDto!) {
                        register_user(userRegisterDto: $userRegisterDto) {
                            id
                        }
                    }`,
                variables: { userRegisterDto: this.form },
              }),
            });

            const data = await response.json();

            if (data.errors) {
              const error = data.errors[0];
              if (
                error.message.includes('User must be at least 18 years old')
              ) {
                this.errors = {
                  birthDate: 'User must be at least 18 years old',
                };
              } else if (
                error.message.includes(
                  'Password and password confirmation do not match'
                )
              ) {
                this.errors = {
                  passwordConfirmation:
                    'Password and password confirmation do not match',
                };
              } else if (error.message.includes('Email is already taken')) {
                this.errors = { email: 'Email is already taken' };
              } else if (
                error.message.includes(
                  'Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)'
                )
              ) {
                this.errors = {
                  password:
                    'Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)',
                };
              } else {
                this.errors = { errorMessage: error };
              }
              return;
            }

            if (data.data?.register_user?.id) {
              localStorage.setItem(
                'successMessage',
                'Successfully Registered!'
              );
              this.$router.push('/login');
            } else {
              this.errors = {
                errorMessage: 'Registration failed! Please check the details.',
              };
            }
          } catch (error) {
            this.errors = { errorMessage: error };
            console.error('Error during registration:', error);
          }
        } else {
          try {
            const response = await fetch(`${BASE_URL}/api/auth/register`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(this.form),
            });

            if (!response.ok) {
              const errorData = await response.json();

              const errorMessage = errorData.message || 'An error occurred.';
              if (
                errorMessage.includes(
                  'Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)'
                )
              ) {
                this.errors = { password: errorMessage };
              } else if (
                errorMessage.includes(
                  'Password and password confirmation do not match'
                )
              ) {
                this.errors = { passwordConfirmation: errorMessage };
              } else if (
                errorMessage.includes('User must be at least 18 years old')
              ) {
                this.errors = { birthDate: errorMessage };
              } else if (errorMessage.includes('Email is already taken')) {
                this.errors = { email: errorMessage };
              } else {
                this.errors = { errorMessage: errorMessage };
              }
              return;
            }

            const data = await response.json();

            if (data.id) {
              localStorage.setItem(
                'successMessage',
                'Successfully Registered!'
              );
              this.$router.push('/login');
            }
          } catch (error) {
            console.error('Error during registration:', error);
            this.errors.errorMessage = error.message;
          }
        }
      },
    },
    async mounted() {
      await this.fetchCountries();
    },
  };
</script>
