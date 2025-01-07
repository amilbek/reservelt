import { createRouter, createWebHistory } from 'vue-router';
import RegisterPage from '../pages/RegisterPage.vue';
import LoginPage from '../pages/LoginPage.vue';
import ProfilePage from '../pages/ProfilePage.vue';
import EditUser from '../pages/EditUser.vue';
import ChangePassword from '../pages/ChangePassword.vue';
import RestaurantSearch from '../pages/RestaurantSearch.vue';
import RestaurantDetails from '../pages/RestaurantDetails.vue';
import ReservationPage from '../components/ReservationPage.vue';

const routes = [
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfilePage,
  },
  {
    path: '/edit-user',
    name: 'Edit User',
    component: EditUser,
  },
  {
    path: '/change-password',
    name: 'Change Password',
    component: ChangePassword,
  },
  {
    path: '/restaurant/:name',
    name: 'RestaurantDetails',
    component: RestaurantDetails,
  },
  {
    path: '/my-reservations',
    name: 'My Reservations',
    component: ReservationPage,
  },
  { path: '/restaurants', name: 'Restaurants', component: RestaurantSearch },
  {
    path: '/',
    redirect: '/restaurants',
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('authToken');
  if (!token) {
    if (
      to.path !== '/restaurants' &&
      to.path !== '/login' &&
      to.path !== '/register' &&
      !to.path.startsWith('/restaurant/')
    ) {
      console.log('Returning');
      return next('/restaurants');
    }
  }
  next();
});

export default router;
