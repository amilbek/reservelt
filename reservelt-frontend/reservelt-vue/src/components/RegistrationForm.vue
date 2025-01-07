<template>
  <div class="container">
    <div class="right-corner-btn">
      <button type="button" class="btn" @click="redirectToRestaurants">
        Go to Restaurants
      </button>
    </div>
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
        <span v-if="errors.birthDate" class="error-message">{{
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
        <span v-if="errors.passwordConfirmation" class="error-message">{{
          errors.passwordConfirmation
        }}</span>
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
      redirectToRestaurants() {
        window.location.href = '/restaurants';
      },
    },
    async mounted() {
      await this.fetchCountries();
    },
  };
</script>

<style scoped>
  body {
    font-family: Arial, sans-serif;
    margin: 50px;
    padding: 50px;
    background-color: #f4f4f4;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }
  .container {
    padding: 100px 30px;
    width: 100%;
  }

  .form-signin-heading {
    text-align: center;
    font-size: 24px;
    margin-bottom: 15px;
    color: #42b883;
  }
  .form-signin p {
    text-align: center;
    color: #42b883;
    margin-bottom: 30px;
  }
  .form-control {
    width: 450px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
  }
  .form-control-select {
    width: 475px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
  }
  .form-control:focus {
    border-color: #42b883;
    outline: none;
  }
  select.form-control {
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
  }
  label {
    display: block;
    margin-bottom: 5px;
    font-weight: 600;
    color: #555;
  }
  .btn-container {
    text-align: center;
  }
  .btn {
    margin: 25px 0;
    padding: 10px 15px;
    font-size: 16px;
    color: #fff;
    background-color: #42b883;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  .btn:hover {
    background-color: #42b883;
  }
  .btn:focus {
    outline: none;
  }
  a {
    color: #007bff;
    text-decoration: none;
  }
  a:hover {
    text-decoration: underline;
  }
  .bottom_btns {
    margin-right: 300px;
  }
  .right-corner-btn {
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 1000;
  }
  .error-message {
    display: block;
    color: red;
    text-align: center;
    margin-bottom: 10px;
  }
</style>
