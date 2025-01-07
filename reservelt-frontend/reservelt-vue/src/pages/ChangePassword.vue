<template>
  <div>
    <nav class="navbar">
      <div class="logo">Reservelt</div>
      <div class="nav-links">
        <button @click="redirectToRestaurants">Go to Restaurants</button>
        <button @click="redirectToMyReservations">My Reservations</button>
        <router-link to="/profile">Profile</router-link>
        <router-link to="/edit-user">Edit User</router-link>
        <button @click="logout">Logout</button>
      </div>
    </nav>
    <div class="container">
      <form @submit.prevent="handleChangePassword" class="form-signin">
        <h2 class="form-signin-heading">Change Password</h2>
        <div v-if="successMessage" class="text-success" style="color: green">
          {{ successMessage }}
        </div>
        <div v-if="errors.errorMessage" class="text-success" style="color: red">
          {{ errors.errorMessage }}
        </div>

        <p>
          <label for="current_password" class="sr-only">Current Password</label>
          <input
            type="password"
            id="current_password"
            v-model="form.currentPassword"
            class="form-control"
            placeholder="Current Password"
            required
          />
          <span v-if="errors.currentPassword" class="error-message">{{
            errors.currentPassword
          }}</span>
        </p>
        <p>
          <label for="new_password" class="sr-only">New Password</label>
          <input
            type="password"
            id="new_password"
            v-model="form.newPassword"
            class="form-control"
            placeholder="New Password"
            required
          />
          <span v-if="errors.newPassword" class="error-message">{{
            errors.newPassword
          }}</span>
        </p>
        <p>
          <label for="new_password_confirmation" class="sr-only"
            >New Password Confirmation</label
          >
          <input
            type="password"
            id="new_password_confirmation"
            v-model="form.newPasswordConfirmation"
            class="form-control"
            placeholder="New Password Confirmation"
            required
          />
          <span v-if="errors.newPasswordConfirmation" class="error-message">{{
            errors.newPasswordConfirmation
          }}</span>
        </p>
        <div class="btn-container">
          <button type="submit" class="btn btn-lg btn-primary btn-block">
            Save Changes
          </button>
          <router-link to="/profile" class="btn btn-warning"
            >Cancel</router-link
          >
        </div>
      </form>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        form: {
          currentPassword: '',
          newPassword: '',
          newPasswordConfirmation: '',
        },
        successMessage: '',
        errors: {},
        BASE_URL: import.meta.env.VITE_BASE_URL,
      };
    },
    methods: {
      async handleChangePassword() {
        const token = localStorage.getItem('authToken');
        if (!token) {
          this.$router.push('/login');
          return;
        }

        const headers = {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        };

        const data = { ...this.form };
        const isGraphQL = window.location.href.includes('graphql');

        try {
          if (isGraphQL) {
            const response = await fetch(`${this.BASE_URL}/graphql`, {
              method: 'POST',
              headers,
              body: JSON.stringify({
                query: `
                mutation changePassword($changeUserPasswordDto: ChangeUserPasswordDto!) {
                  change_password(changeUserPasswordDto: $changeUserPasswordDto)
                }
              `,
                variables: { changeUserPasswordDto: data },
              }),
            });

            const responseData = await response.json();

            if (responseData.errors) {
              const error = responseData.errors[0] || 'An error occurred.';
              if (error.message.includes('Current password does not match')) {
                this.errors = {
                  currentPassword: 'Current password does not match',
                };
              } else if (
                error.message.includes(
                  'New password and password confirmation do not match'
                )
              ) {
                this.errors = {
                  newPasswordConfirmation:
                    'New password and password confirmation do not match',
                };
              } else if (
                error.message.includes(
                  'Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)'
                )
              ) {
                this.errors = {
                  newPassword:
                    'Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)',
                };
              } else {
                this.errors = { errorMessage: error.message };
              }
              return;
            }

            this.successMessage = 'Password changed successfully!';
            localStorage.setItem('successMessage', this.successMessage);
            this.$router.push('/profile');
          } else {
            const response = await fetch(
              `${this.BASE_URL}/api/users/change-password`,
              {
                method: 'PUT',
                headers,
                body: JSON.stringify(data),
              }
            );

            if (!response.ok) {
              const errorData = await response.json();

              const errorMessage = errorData.message || 'An error occurred.';
              if (errorMessage.includes('Current password does not match')) {
                this.errors = { currentPassword: errorMessage };
              } else if (
                errorMessage.includes(
                  'New password and password confirmation do not match'
                )
              ) {
                this.errors = { newPasswordConfirmation: errorMessage };
              } else if (
                errorMessage.includes(
                  'Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)'
                )
              ) {
                this.errors = { newPassword: errorMessage };
              } else {
                this.errors = { errorMessage: errorMessage };
              }
              return;
            }

            this.successMessage = 'Password changed successfully!';
            localStorage.setItem('successMessage', this.successMessage);
            this.$router.push('/profile');
          }
        } catch (error) {
          console.error('Error:', error);
          this.errorMessage =
            error.message || 'An error occurred while changing the password.';
        }
      },
      redirectToLogin() {
        this.$router.push('/login');
      },
      redirectToRestaurants() {
        window.location.href = '/restaurants';
      },
      redirectToMyReservations() {
        window.location.href = '/my-reservations';
      },
      logout() {
        localStorage.removeItem('authToken');
        localStorage.setItem(
          'successMessage',
          'You have been logged out successfully!'
        );
        this.redirectToLogin();
      },
    },
    mounted() {
      const successMessage = localStorage.getItem('successMessage');
      if (successMessage) {
        this.successMessage = successMessage;
        localStorage.removeItem('successMessage');
      }
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
  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
    background-color: #333;
    color: white;
  }

  .navbar .nav-links {
    display: flex;
    gap: 15px;
  }

  .navbar button,
  .navbar a {
    color: white;
    text-decoration: none;
    padding: 8px 12px;
    border-radius: 5px;
    background-color: #555;
    border: none;
    cursor: pointer;
  }

  .navbar button:hover,
  .navbar a:hover {
    background-color: #777;
  }
  .container {
    padding: 40px 30px;
    max-width: 800px;
    width: 100%;
    margin: 50px auto;
    border-radius: 8px;
    background-color: #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin: 20px 0;
    text-align: center;
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
  .btn-container {
    margin-top: 20px;
    text-align: center;
    display: flex;
    justify-content: center;
    gap: 20px;
  }
  .error-message {
    display: block;
    color: red;
    text-align: center;
    margin-bottom: 10px;
  }
</style>
