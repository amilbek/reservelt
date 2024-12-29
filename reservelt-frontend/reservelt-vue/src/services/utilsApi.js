const BASE_URL = import.meta.env.VITE_BASE_URL;

export async function getCountries() {
  const response = await fetch(`${BASE_URL}/auth/country-list`);
  return response.json();
}

export async function getCities(countryId) {
  const response = await fetch(`${BASE_URL}/auth/${countryId}/city-list`);
  return response.json();
}
