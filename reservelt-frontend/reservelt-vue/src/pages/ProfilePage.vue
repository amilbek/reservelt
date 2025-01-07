<template>
  <div>
    <nav class="navbar">
      <div class="logo">My Profile</div>
      <div class="nav-links">
        <button @click="redirectToRestaurants">Go to Restaurants</button>
        <button @click="redirectToMyReservations">My Reservations</button>
        <router-link to="/edit-user">Edit Account</router-link>
        <router-link to="/change-password">Change Password</router-link>
        <button @click="logout">Logout</button>
        <button @click="showDeleteModal = true">Delete Account</button>
      </div>
    </nav>

    <div class="container">
      <h2 v-if="user" id="greeting">Welcome, {{ user.firstName }}!</h2>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      <div v-if="successMessage" class="success-message">
        {{ successMessage }}
      </div>

      <div v-if="user" class="profile-info">
        <table>
          <tbody>
            <tr>
              <th>First Name</th>
              <td>{{ user.firstName }}</td>
            </tr>
            <tr>
              <th>Last Name</th>
              <td>{{ user.lastName }}</td>
            </tr>
            <tr>
              <th>Birth Date</th>
              <td>{{ user.birthDate }}</td>
            </tr>
            <tr>
              <th>Email</th>
              <td>{{ user.email }}</td>
            </tr>
            <tr>
              <th>Country</th>
              <td>{{ user.country.name }}</td>
            </tr>
            <tr>
              <th>City</th>
              <td>{{ user.city.name }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <DeleteModal
        :showModal="showDeleteModal"
        @confirm="confirmDelete"
        @cancel="showDeleteModal = false"
      />
    </div>
  </div>
</template>

<script>
  import DeleteModal from '../components/DeleteModal.vue';

  export default {
    components: {
      DeleteModal,
    },
    data() {
      return {
        user: null,
        errorMessage: '',
        successMessage: '',
        showDeleteModal: false,
        isGraphQL: window.location.href.includes('graphql'),
        BASE_URL: import.meta.env.VITE_BASE_URL,
      };
    },
    methods: {
      async fetchUserProfile() {
        const token = localStorage.getItem('authToken');
        if (!token) {
          this.redirectToLogin();
          return;
        }

        const headers = {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        };

        try {
          if (this.isGraphQL) {
            const response = await fetch(`${this.BASE_URL}/graphql`, {
              method: 'POST',
              headers,
              body: JSON.stringify({
                query: `
                query {
                  user {
                    id
                    firstName
                    lastName
                    birthDate
                    email
                    country {
                      name
                    }
                    city {
                      name
                    }
                  }
                }
              `,
              }),
            });

            const responseData = await response.json();

            if (!response.ok || responseData.errors) {
              throw new Error(
                responseData.errors?.[0]?.message || 'Failed to fetch profile.'
              );
            }

            this.user = responseData.data.user;
          } else {
            const response = await fetch(`${this.BASE_URL}/api/users`, {
              headers,
            });

            if (!response.ok) {
              throw new Error(`HTTP error ${response.status}`);
            }

            this.user = await response.json();
          }
        } catch (error) {
          this.handleError(error);
        }
      },
      handleError(err) {
        this.errorMessage = err.message || 'An error occurred.';
      },
      logout() {
        localStorage.removeItem('authToken');
        localStorage.setItem(
          'successMessage',
          'You have been logged out successfully!'
        );
        this.redirectToLogin();
      },
      redirectToLogin() {
        this.$router.push('/login');
      },
      async confirmDelete() {
        const token = localStorage.getItem('authToken');
        if (!token) {
          this.redirectToLogin();
          return;
        }

        const headers = {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        };
        const deleteURL = this.isGraphQL
          ? `${this.BASE_URL}/graphql`
          : `${this.BASE_URL}/api/users/delete`;
        const deleteBody = this.isGraphQL
          ? JSON.stringify({ query: `mutation { delete_user }` })
          : null;

        try {
          const response = await fetch(deleteURL, {
            method: this.isGraphQL ? 'POST' : 'DELETE',
            headers,
            body: deleteBody,
          });

          if (!response.ok) {
            throw new Error('Failed to delete account.');
          }

          localStorage.removeItem('authToken');
          localStorage.setItem(
            'successMessage',
            'Your account has been deleted successfully!'
          );
          this.redirectToLogin();
        } catch (error) {
          this.handleError(error);
        }
      },
      redirectToRestaurants() {
        window.location.href = '/restaurants';
      },
      redirectToMyReservations() {
        window.location.href = '/my-reservations';
      },
    },
    async mounted() {
      const successMessage = localStorage.getItem('successMessage');
      if (successMessage) {
        this.successMessage = successMessage;
        localStorage.removeItem('successMessage');
      }

      await this.fetchUserProfile();
    },
  };
</script>
<style scoped>
  body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    box-sizing: border-box;
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

  /* Main content styles */
  .container {
    padding: 20px;
    max-width: 800px;
    margin: 50px auto;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color: #f9f9f9;
  }

  h2 {
    text-align: center;
    margin-bottom: 20px;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }

  th,
  td {
    padding: 10px;
    border-bottom: 1px solid #ddd;
    text-align: left;
  }

  th {
    width: 30%;
  }

  .error-message {
    color: red;
    text-align: center;
    margin-bottom: 10px;
  }

  .success-message {
    color: green;
    text-align: center;
    margin-bottom: 10px;
  }

  .btn {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-right: 10px;
  }

  .btn-primary {
    background-color: #007bff;
    color: white;
  }

  .btn-danger {
    background-color: #dc3545;
    color: white;
  }

  .btn-primary:hover,
  .btn-danger:hover {
    opacity: 0.8;
  }
</style>
