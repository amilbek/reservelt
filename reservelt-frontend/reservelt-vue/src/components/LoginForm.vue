<template>
  <div class="container">
    <div class="right-corner-btn">
      <button type="button" class="btn" @click="redirectToRestaurants">Go to Restaurants</button>
    </div>
    <h2 class="form-signin-heading">Login</h2>
    <span v-if="successMessage" style="color: green">{{ successMessage }}</span>
    <span v-if="errorMessage" style="color: red">{{ errorMessage }}</span>

    <form @submit.prevent="handleSubmit">
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
      </p>
      <div class="btn-container">
        <button type="submit" class="btn">Login</button>
      </div>
    </form>

    <div class="bottom_btns">
      <p><a href="/register">Do you want to register?</a></p>
    </div>
  </div>
</template>

<script>
  const BASE_URL = import.meta.env.VITE_BASE_URL;

  export default {
    data() {
      return {
        form: {
          email: '',
          password: '',
        },
        errorMessage: '',
        successMessage: '',
      };
    },
    methods: {
      async handleSubmit() {
        const payload = { ...this.form };

        try {
          if (this.isGraphql()) {
            const response = await fetch(`${BASE_URL}/graphql`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                query: `
                mutation LoginUser($email: String!, $password: String!) {
                  login_user(userLoginDto: { email: $email, password: $password })
                }
              `,
                variables: {
                  email: payload.email,
                  password: payload.password,
                },
              }),
            });

            const responseData = await response.json();

            if (responseData.errors) {
              const firstErrorMessage =
                responseData.errors?.[0]?.message || 'Login failed!';
              const cleanErrorMessage = firstErrorMessage
                .replace('Invalid input: ', '')
                .trim();
              throw new Error(cleanErrorMessage);
            }

            const token = responseData.data?.login_user;
            if (token) {
              localStorage.setItem('authToken', token);
              window.location.href = '/profile';
            }
          } else {
            const response = await fetch(`${BASE_URL}/api/auth/login`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(payload),
            });

            if (!response.ok) {
              const errorData = await response.json();
              throw new Error(errorData.message || 'Login failed!');
            }

            const token = await response.text();
            if (token) {
              localStorage.setItem('authToken', token);
              window.location.href = '/profile';
            }
          }
        } catch (error) {
          console.error('Error during login:', error);
          localStorage.removeItem('successMessage');
          this.errorMessage =
            error.message || 'An error occurred during login.';
          this.successMessage = '';
        }
      },
      isGraphql() {
        return window.location.href.includes('graphql');
      },
      redirectToRestaurants() {
        window.location.href = '/restaurants';
      },
    },
    mounted() {
      const storedMessage = localStorage.getItem('successMessage');
      if (storedMessage) {
        this.successMessage = storedMessage;
        localStorage.removeItem('successMessage');
      }
    },
  };
</script>
