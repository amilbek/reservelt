const BASE_URL = import.meta.env.VITE_BASE_URL;

export async function registerUser(userData) {
  const response = await fetch(`${BASE_URL}/api/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw errorData.errors || { general: 'Registration failed!' };
  }

  return response.json();
}
