<template>
  <div class="container">
    <div class="right-corner-btn">
      <router-link to="/profile" class="btn btn-success mb-3">Profile</router-link>
    </div>

    <h1>Your Table Reservations</h1>

    <div v-if="reservations.length === 0" class="no-reservations">
      <p>You have no table reservations.</p>
    </div>

    <div v-if="reservations.length > 0">
      <table class="table table-bordered">
        <thead>
        <tr>
          <th>#</th>
          <th>Restaurant Name</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(reservation, index) in reservations" :key="reservation.id">
          <td>{{ index + 1 }}</td>
          <td>{{ reservation.restaurantName }}</td>
          <td>{{ getReadableStatus(reservation.tableReservationStatus) }}</td>
          <td>
            <div v-if="reservation.tableReservationStatus === 'RESERVED'">
              <button @click="changeStatus(reservation.id, 'IN_PROCESS')" class="btn btn-primary btn-sm">
                Mark as In Process
              </button>
              <button @click="changeStatus(reservation.id, 'CANCELLED')" class="btn btn-danger btn-sm">
                Cancel
              </button>
            </div>

            <div v-if="reservation.tableReservationStatus === 'IN_PROCESS'">
              <button @click="changeStatus(reservation.id, 'COMPLETED')" class="btn btn-success btn-sm">
                Mark as Completed
              </button>
            </div>

            <div v-if="reservation.tableReservationStatus === 'COMPLETED'">
              <span class="text-success">No further actions</span>
            </div>

            <div v-if="reservation.tableReservationStatus === 'CANCELLED'">
              <span class="text-muted">No further actions</span>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
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
        const response = await fetch(`${this.BASE_URL}/api/restaurants/my-reservations`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
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
        const response = await fetch(`${this.BASE_URL}/api/restaurants/change-status`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded', // Use form URL encoding
            'Authorization': `Bearer ${token}`
          },
          body: new URLSearchParams({
            reservationId: reservationId,
            newStatus: newStatus
          })
        });

        if (!response.ok) throw new Error('Failed to change status');
        this.fetchReservations();
      } catch (error) {
        console.error('Error changing status:', error);
      }
    }
  },
  mounted() {
    this.fetchReservations();
  }
};
</script>

