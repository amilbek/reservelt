<template>
  <div class="restaurant-details-outer">
  <div class="restaurant-name">
    <h1 class="restaurant-title">{{ restaurant.name }}</h1>
  </div>
  <div>
    <section class="restaurant-details-container">
      <div v-if="restaurant" class="restaurant-details-content">
        <p><strong>Phone:</strong> {{ restaurant.phoneNumber }}</p>
        <p><strong>Address:</strong> {{ restaurant.address }}</p>
        <p><strong>Rating:</strong> {{ restaurant.rating }}</p>

        <!-- Explore Menu Section -->
        <section class="explore__menu__container">
          <h1 class="explore__menu__text">Explore Our Menu</h1>
          <div class="explore__menu__lists">
            <div
              v-for="food in restaurant.foods"
              :key="food.id"
              class="food__card"
            >
              <div class="food__card__description">
                <h3 class="food__title">{{ food.name }}</h3>
                <p>{{ food.description }}</p>
                <p style="font-weight: bold;">€ {{ food.price }}</p>
              </div>
            </div>
          </div>
        </section>
      </div>
      <div v-else-if="isLoading">Loading...</div>
      <div v-else>Failed to load restaurant details.</div>
      <div class="Buttons-control">
        <button class="nav__item button__primary" @click="goBack">Go Back</button>
        <button class="nav__item button__primary" @click="">Reserve Seats</button>
      </div>
    </section>
  </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      restaurant: null,
      isLoading: true,
      foodImage: {}, // Object to store food names and their image URLs
    };
  },
  created() {
    this.fetchRestaurantDetails();
  },
  methods: {
        async fetchRestaurantDetails() {
      const restaurantName = this.$route.params.name;
      const apiUrl = `http://localhost:8080/api/restaurants/search?name=${restaurantName}`;

      try {
        const response = await fetch(apiUrl); // Use the proxy to bypass CORS
        if (response.ok) {
          const data = await response.json();
          this.restaurant = Array.isArray(data) && data.length > 0 ? data[0] : null;
        } else {
          console.error("API Error:", response.statusText);
          alert("Error fetching restaurant details.");
        }
      } catch (error) {
        console.error("Error fetching restaurant details:", error);
        alert("Error fetching restaurant details.");
      } finally {
        this.isLoading = false;
      }
    },
    


    goBack() {
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.restaurant-details-outer{
  width:100%;
}

.restaurant-name{
  background:black;
  color:white;
  height:80px;
  display: flex;
  align-content: center;
  justify-content: center;
}

.restaurant-details-container {
  padding: 50px;
  text-align: center;
  background-color: #f9f9f9;
}

.restaurant-title {
  font-size: 32px;
  margin-bottom: 20px;
}

/* Explore Menu Section */
.explore__menu__container {
  margin-top: 30px;
  text-align: center;
}

.explore__menu__text {
  font-size: 28px;
  color: #333;
  margin-bottom: 20px;
}

.explore__menu__lists {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
}

.food__card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 300px;
  text-align: center;
}

.food__card__image__container {
  width: 100%;
  overflow: hidden;
}

.food__image {
  width: 100%;
  height: auto;
  border-bottom: 1px solid #ddd;
}

.food__card__description {
  padding: 15px;
}

.food__title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.button__primary {
  background-color: #ff4500;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 30px;
}

.button__primary:hover {
  background-color: #ff6b35;
}

.Buttons-control{
  display:flex;
  justify-content: space-between;
}
</style>

     
