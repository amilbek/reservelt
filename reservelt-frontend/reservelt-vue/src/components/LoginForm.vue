<template>
  <div class="container">
    <div class="right-corner-btn">
      <button type="button" class="btn" @click="redirectToRestaurants">
        Go to Restaurants
      </button>
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
</style>
