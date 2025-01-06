<template>
  <div class="container mt-5">
    <div class="right-corner-btn">
      <button type="button" class="btn" @click="redirectToRestaurants">Go to Restaurants</button>
      <button type="button" class="btn" @click="redirectToMyReservations">My Reservations</button>
    </div>
    <div id="userEditForm" class="alert alert-info mt-2">
      <h2 class="form-signin-heading">Edit User</h2>
      <div v-if="successMessage" style="color: green">{{ successMessage }}</div>
      <div v-if="errors.errorMessage" class="text-success" style="color: red">
        {{ errors.errorMessage }}
      </div>
      <form
        id="editUserForm"
        class="form-signin"
        @submit.prevent="handleSubmit"
      >
        <table>
          <tbody>
            <tr>
              <th><label for="firstName">First Name</label></th>
              <td>
                <input
                  type="text"
                  id="firstName"
                  v-model="form.firstName"
                  class="form-control"
                  placeholder="First Name"
                  required
                />
              </td>
            </tr>
            <tr>
              <th><label for="lastName">Last Name</label></th>
              <td>
                <input
                  type="text"
                  id="lastName"
                  v-model="form.lastName"
                  class="form-control"
                  placeholder="Last Name"
                  required
                />
              </td>
            </tr>
            <tr>
              <th><label for="birthDate">Birth Date</label></th>
              <td>
                <input
                  type="date"
                  id="birthDate"
                  v-model="form.birthDate"
                  class="form-control"
                  required
                />
                <span
                  v-if="errors.birthDate"
                  class="text-danger"
                  style="color: red"
                  >{{ errors.birthDate }}</span
                >
              </td>
            </tr>
            <tr>
              <th><label for="country">Country</label></th>
              <td>
                <select
                  id="country"
                  v-model="form.country"
                  @change="fetchCities(form.country)"
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
              </td>
            </tr>
            <tr>
              <th><label for="city">City</label></th>
              <td>
                <select
                  id="city"
                  v-model="form.city"
                  class="form-control-select"
                >
                  <option value="">Choose City</option>
                  <option
                    v-for="city in cities"
                    :key="city.id"
                    :value="city.id"
                  >
                    {{ city.name }}
                  </option>
                </select>
              </td>
            </tr>
          </tbody>
        </table>
        <button
          id="submitEditButton"
          class="btn btn-lg btn-primary btn-block"
          type="submit"
        >
          Save Changes
        </button>
        <router-link to="/profile" class="btn btn-warning">Cancel</router-link>
      </form>
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
        },
        countries: [],
        cities: [],
        successMessage: '',
        errors: {},
      };
    },
    methods: {
      async fetchUserData() {
        const token = localStorage.getItem('authToken');
        if (!token) {
          this.$router.push('/login');
          return;
        }

        const headers = {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        };

        try {
          if (this.isGraphQL()) {
            const response = await fetch(`${BASE_URL}/graphql`, {
              method: 'POST',
              headers,
              body: JSON.stringify({
                query: `
                query {
                  user {
                    firstName
                    lastName
                    birthDate
                    country { id name }
                    city { id name }
                  }
                }
              `,
              }),
            });

            const responseData = await response.json();

            if (responseData.errors) {
              throw new Error(
                responseData.errors?.[0]?.message || 'Failed to fetch profile.'
              );
            }

            this.populateForm(responseData.data.user);
          } else {
            const response = await fetch(`${BASE_URL}/api/users`, { headers });

            if (!response.ok) {
              throw new Error('Failed to fetch user data.');
            }

            const user = await response.json();
            this.populateForm(user);
          }
        } catch (error) {
          this.errorMessage = error.message;
        }
      },
      populateForm(user) {
        this.form.firstName = user.firstName;
        this.form.lastName = user.lastName;
        this.form.birthDate = user.birthDate;
        this.fetchCountries(user.country.id);
        this.fetchCities(user.country.id, user.city.id);
      },
      async fetchCountries(selectedCountryId = null) {
        try {
          const response = await fetch(`${BASE_URL}/auth/country-list`);
          const countries = await response.json();
          this.countries = countries;

          if (selectedCountryId) {
            this.form.country = selectedCountryId;
          }
        } catch (error) {
          console.error('Error fetching countries:', error);
        }
      },
      async fetchCities(countryId, selectedCityId = null) {
        if (!countryId) {
          this.cities = [];
          this.form.city = '';
          return;
        }

        try {
          const response = await fetch(
            `${BASE_URL}/auth/${countryId}/city-list`
          );
          const cities = await response.json();
          this.cities = cities;

          if (selectedCityId) {
            this.form.city = selectedCityId;
          }
        } catch (error) {
          console.error('Error fetching cities:', error);
          this.cities = [];
        }
      },
      async handleSubmit() {
        const token = localStorage.getItem('authToken');
        if (!token) {
          this.$router.push('/login');
          return;
        }

        const headers = {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        };

        try {
          if (this.isGraphQL()) {
            const payload = {
              ...this.form,
              country: parseInt(this.form.country, 10),
              city: parseInt(this.form.city, 10),
            };

            const response = await fetch(`${BASE_URL}/graphql`, {
              method: 'POST',
              headers,
              body: JSON.stringify({
                query: `
                mutation($userEditDto: UserEditDto!) {
                  edit_user(userEditDto: $userEditDto) {
                    id
                  }
                }
              `,
                variables: { userEditDto: payload },
              }),
            });

            const responseData = await response.json();

            if (responseData.errors) {
              const error = responseData.errors[0] || 'An error occurred.';

              if (
                error.message.includes('User must be at least 18 years old')
              ) {
                this.errors = {
                  birthDate: 'User must be at least 18 years old',
                };
              } else {
                this.errors = { general: error.message };
              }
              return;
            }

            localStorage.setItem(
              'successMessage',
              'User details updated successfully!'
            );
            this.$router.push('/profile');
          } else {
            const response = await fetch(`${BASE_URL}/api/users/edit`, {
              method: 'PUT',
              headers,
              body: JSON.stringify(this.form),
            });

            if (!response.ok) {
              const errorData = await response.json();

              const errorMessage = errorData.message || 'An error occurred.';
              if (errorMessage.includes('User must be at least 18 years old')) {
                this.errors = { birthDate: errorMessage };
              } else {
                this.errors = { general: errorMessage };
              }
              return;
            }

            localStorage.setItem(
              'successMessage',
              'User details updated successfully!'
            );
            this.$router.push('/profile');
          }
        } catch (error) {
          this.errorMessage = error.message;
        }
      },
      isGraphQL() {
        return window.location.href.includes('graphql');
      },
      redirectToRestaurants() {
        window.location.href = '/restaurants';
      },
      redirectToMyReservations() {
        window.location.href = '/my-reservations';
      },
    },
    async mounted() {
      await this.fetchUserData();
    },
  };
</script>
