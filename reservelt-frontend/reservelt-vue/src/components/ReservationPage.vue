<template>
  <div>
    <nav class="navbar">
      <div class="logo">Reservelt</div>
      <div class="nav-links">
        <button @click="redirectToRestaurants">Go to Restaurants</button>
        <router-link to="/profile">Profile</router-link>
        <router-link to="/edit-user">Edit User</router-link>
        <router-link to="/change-password">Change Password</router-link>
        <button @click="logout">Logout</button>
      </div>
    </nav>
    <div class="container">
      <h1 class="heading">Your Table Reservations</h1>

      <div v-if="reservations.length === 0" class="no-reservations">
        <p>You have no table reservations.</p>
      </div>

      <div v-if="reservations.length > 0" class="table-container">
        <table class="table">
          <thead>
            <tr>
              <th>#</th>
              <th>Restaurant Name</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(reservation, index) in reservations"
              :key="reservation.id"
            >
              <td>{{ index + 1 }}</td>
              <td>{{ reservation.restaurantName }}</td>
              <td>
                <span
                  :class="`status ${reservation.tableReservationStatus.toLowerCase()}`"
                >
                  {{ getReadableStatus(reservation.tableReservationStatus) }}
                </span>
              </td>
              <td>
                <div class="actions">
                  <button
                    v-if="reservation.tableReservationStatus === 'RESERVED'"
                    @click="changeStatus(reservation.id, 'IN_PROCESS')"
                    class="btn btn-primary"
                  >
                    Mark as In Process
                  </button>
                  <button
                    v-if="reservation.tableReservationStatus === 'RESERVED'"
                    @click="changeStatus(reservation.id, 'CANCELLED')"
                    class="btn btn-danger"
                  >
                    Cancel
                  </button>

                  <button
                    v-if="reservation.tableReservationStatus === 'IN_PROCESS'"
                    @click="changeStatus(reservation.id, 'COMPLETED')"
                    class="btn btn-success"
                  >
                    Mark as Completed
                  </button>

                  <span
                    v-if="reservation.tableReservationStatus === 'COMPLETED'"
                    class="text-success"
                  >
                    No further actions
                  </span>
                  <span
                    v-if="reservation.tableReservationStatus === 'CANCELLED'"
                    class="text-muted"
                  >
                    No further actions
                  </span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        reservations: [],
        BASE_URL: import.meta.env.VITE_BASE_URL,
      };
    },
    methods: {
      async fetchReservations() {
        const token = localStorage.getItem('authToken');
        if (!token) {
          this.redirectToLogin();
          return;
        }

        try {
          const response = await fetch(
            `${this.BASE_URL}/api/restaurants/my-reservations`,
            {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
              },
            }
          );
          if (!response.ok) throw new Error('Failed to fetch reservations');
          if (response.status !== 204) {
            this.reservations = await response.json();
          }
        } catch (error) {
          console.error('Error fetching reservations:', error);
          alert('Failed to load reservations. Please try again later.');
        }
      },
      getReadableStatus(status) {
        switch (status) {
          case 'RESERVED':
            return 'Reserved';
          case 'IN_PROCESS':
            return 'In Process';
          case 'COMPLETED':
            return 'Completed';
          case 'CANCELLED':
            return 'Cancelled';
          default:
            return 'Unknown';
        }
      },
      async changeStatus(reservationId, newStatus) {
        const token = localStorage.getItem('authToken');
        if (!token) {
          this.redirectToLogin();
          return;
        }
        try {
          const response = await fetch(
            `${this.BASE_URL}/api/restaurants/change-status`,
            {
              method: 'POST',
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded', // Use form URL encoding
                Authorization: `Bearer ${token}`,
              },
              body: new URLSearchParams({
                reservationId: reservationId,
                newStatus: newStatus,
              }),
            }
          );

          if (!response.ok) throw new Error('Failed to change status');
          this.fetchReservations();
        } catch (error) {
          console.error('Error changing status:', error);
        }
      },
      redirectToLogin() {
        this.$router.push('/login');
      },
      redirectToRestaurants() {
        window.location.href = '/restaurants';
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
      this.fetchReservations();
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

  .container {
    padding: 20px;
    max-width: 800px;
    margin: 50px auto;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color: #f9f9f9;
  }

  h1 {
    text-align: center;
    color: #42b883;
    margin-bottom: 30px;
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

  .no-reservations {
    text-align: center;
    font-size: 18px;
    color: #666;
    padding: 20px;
    background-color: #f8f9fa;
    border: 1px solid #ddd;
    border-radius: 8px;
  }

  .table-container {
    overflow-x: auto;
    margin-top: 20px;
  }

  .table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }

  .table th,
  .table td {
    padding: 12px 15px;
    border: 1px solid #ddd;
    text-align: center;
  }

  .table th {
    background-color: #42b883;
    color: white;
  }

  .table td {
    background-color: #f8f9fa;
  }

  .actions {
    display: flex;
    gap: 10px;
    justify-content: center;
  }

  .btn {
    padding: 8px 12px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 14px;
  }

  .btn-primary {
    background-color: #42b883;
    color: white;
  }

  .btn-danger {
    background-color: #dc3545;
    color: white;
  }

  .btn-danger:hover {
    background-color: #c82333;
  }

  .btn-success {
    background-color: #28a745;
    color: white;
  }

  .btn-success:hover {
    background-color: #218838;
  }

  /* Status labels */
  .status {
    padding: 5px 10px;
    border-radius: 5px;
    font-weight: bold;
    text-transform: capitalize;
  }

  .status.reserved {
    background-color: #ffc107;
    color: #856404;
  }

  .status.in_process {
    background-color: #17a2b8;
    color: #0c5460;
  }

  .status.completed {
    background-color: #28a745;
    color: white;
  }

  .status.cancelled {
    background-color: #dc3545;
    color: white;
  }
</style>
