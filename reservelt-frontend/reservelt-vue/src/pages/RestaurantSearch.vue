<template>
  <div class="container">
    <h1>Search for Restaurants</h1>

    <!-- Search Form -->
    <form @submit.prevent="searchRestaurants" class="search-form">
      <div class="form-group">
        <input
          type="text"
          id="restaurantName"
          v-model="searchQuery"
          @input="clearResults"
          placeholder="Enter restaurant name"
        />
      </div>
      <button type="submit" class="search-button">Search</button>
    </form>

    <!-- Search Results -->
    <h1>Search Results</h1>
    <div v-if="isLoading">Loading...</div>
    <div v-else-if="restaurants.length === 0">
      <h5>No restaurants found.</h5>
    </div>

    <div v-else class="search-results">
      <ul>
        <li v-for="restaurant in restaurants" :key="restaurant.name">
          <h2>{{ restaurant.name }}</h2>
          <p>Phone: {{ restaurant.phoneNumber }}</p>
          <p>Address: {{ restaurant.address }}</p>
          <p>Rating: {{ restaurant.rating }}</p>
          <button @click="navigateToRestaurantDetails(restaurant.name)" class="more-button">
            More
          </button>


          <h3>Foods:</h3>
          <div v-if="restaurant.foods && restaurant.foods.length > 0">
            <table class="table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Price</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="food in restaurant.foods" :key="food.id">
                  <td>{{ food.id }}</td>
                  <td>{{ food.name }}</td>
                  <td>{{ food.description }}</td>
                  <td>€ {{ food.price }}</td>
                </tr>
              </tbody>
            </table>

            
          </div>
          <p v-else>No foods available for this restaurant.</p>
        </li>
      </ul>
    </div>
  </div>
</template>


<script>
  export default {
    data() {
      return {
        searchQuery: '',
        restaurants: [],
        isLoading: false,
      };
    },
    methods: {
      async searchRestaurants() {
        this.isLoading = true;
        try {
          const response = await fetch(
            `http://localhost:8080/api/restaurants/search?name=${this.searchQuery}`
          );
          if (response.ok) {
            const data = await response.json();

            // Sanitize data
            this.restaurants = Array.isArray(data)
              ? data.map((restaurant) => ({
                  ...restaurant,
                  foods: Array.isArray(restaurant.foods) ? restaurant.foods : [],
                }))
              : [];
          } else {
            alert('Error fetching data');
          }
        } catch (error) {
          console.error('Error fetching restaurants:', error);
          alert('Error fetching restaurants.');
        } finally {
          this.isLoading = false;
        }
      },
      clearResults() {
        if (!this.searchQuery.trim()) {
          this.restaurants = [];
        }
      },
      navigateToRestaurantDetails(restaurantName) {
        const encodedName = encodeURIComponent(restaurantName);
        const baseUrl = `${window.location.origin}/restaurant/${encodedName}`;
        window.location.href = baseUrl;
      },
    },
  };
</script>


<style scoped>
  body {
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
    margin: 0;
    padding: 0;
  }

  .container {
    width: 80%;
    margin: 0 auto;
    padding: 20px;
  }

  .search-results {
    width: max-content;
    height: auto;
  }

  h1 {
    text-align: center;
    color: #42b883;
    margin-bottom: 30px;
  }

  h3 {
    text-align: left;
  }

  .search-form {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    margin-bottom: 30px;
  }

  .search-form .form-group {
    display: inline-block;
    margin-right: 15px;
  }

  .search-form input {
    width: 500px;
    padding: 10px;
    border: 2px solid #ccc;
    border-radius: 5px;
    font-size: 16px;
  }

  .search-form button {
    padding: 10px 20px;
    background-color: #42b883;
    color: white;
    border: none;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .search-form button:hover {
    background-color: #366f53;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    background-color: #fff;
    margin-bottom: 20px;
    padding: 15px;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  }

  li table {
    width: 100%;
    margin-top: 20px;
    border-collapse: collapse;
  }

  li table th,
  li table td {
    padding: 12px;
    border: 1px solid #ddd;
    text-align: left;
  }

  li table th {
    background-color: #f1f1f1;
    color: #333;
  }

  li table tr:nth-child(even) {
    background-color: #f9f9f9;
  }

  .no-results {
    text-align: left;
    font-size: 18px;
    color: #888;
  }
</style>
