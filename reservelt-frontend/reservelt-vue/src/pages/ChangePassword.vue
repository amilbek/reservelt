<template>
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
        <span v-if="errors.currentPassword" style="color: red">{{
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
        <span v-if="errors.newPassword" style="color: red">{{
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
        <span v-if="errors.newPasswordConfirmation" style="color: red">{{
          errors.newPasswordConfirmation
        }}</span>
      </p>
      <button type="submit" class="btn btn-lg btn-primary btn-block">
        Change
      </button>
      <router-link to="/profile" class="btn btn-warning">Cancel</router-link>
    </form>
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
