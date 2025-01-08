const BASE_URL = "http://localhost:8080";

document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('authToken');
    if (!token) {
        window.location.href = '/login';
        return;
    }

    const successMessage = document.getElementById('successMessage');
    const errorMessage = document.getElementById('errorMessage');

    successMessage.style.display = 'none';
    errorMessage.style.display = 'none';

    const isGraphql = isGraphQL();

    try {
        await fetchUserData(token, isGraphql);
    } catch (error) {
        console.error('Error fetching user data:', error);
        errorMessage.textContent = error.message || 'Failed to fetch user data.';
        errorMessage.style.display = 'block';
    }

    document.getElementById('editUserForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const birthDate = document.getElementById('birthDate').value;
        const country = document.getElementById('country').value;
        const city = document.getElementById('city').value;

        const data = {
            firstName,
            lastName,
            birthDate,
            country,
            city,
        };

        if (isGraphql) {
            data.country = parseInt(data.country, 10);
            data.city = parseInt(data.city, 10);
        }

        try {
            if (isGraphql) {
                const response = await fetch(`${BASE_URL}/graphql`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        query: `
                        mutation($userEditDto: UserEditDto!) {
                            edit_user(userEditDto: $userEditDto) {
                                id
                                firstName
                                lastName
                                email
                                country {
                                    id
                                    name
                                }
                                city {
                                    id
                                    name
                                }
                            }
                        }`,
                        variables: { userEditDto: data },
                    }),
                });
        
                const responseData = await response.json();
        
                if (responseData.errors) {
                    handleFieldErrors(responseData.errors);
                    return;
                }
        
                if (responseData.data?.edit_user?.id) {
                    localStorage.setItem('successMessage', 'User details updated successfully!');
                    window.location.href = '/profile';
                } else {
                    throw new Error('Updating user data failed! Please check the details.');
                }
            } else {
                const response = await fetch(`${BASE_URL}/api/users/edit`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data),
                });
        
                if (!response.ok) {
                    const errorData = await response.json();
                    handleFieldErrors([{ message: errorData.message }]);
                    return;
                }
        
                localStorage.setItem('successMessage', 'User details updated successfully!');
                window.location.href = '/profile';
            }
        } catch (error) {
            console.error('Error updating user data:', error);
            errorMessage.textContent = error.message || 'An error occurred while updating user details.';
            errorMessage.style.display = 'block';
        }        
    });

    document.getElementById("logoutButton").addEventListener("click", () => {
        localStorage.removeItem("authToken");
        localStorage.setItem("successMessage", "You have been logged out successfully!");
        window.location.href = "/login";
    });
});

function isGraphQL() {
    return window.location.href.includes('graphql');
}

async function fetchUserData(token, isGraphql) {
    if (isGraphql) {
        const response = await fetch(`${BASE_URL}/graphql`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
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
                            id
                            name
                        }
                        city {
                            id
                            name
                        }
                    }
                }
                `,
            }),
        });

        const responseData = await response.json();

        if (!response.ok || responseData.errors) {
            throw new Error(responseData.errors?.[0]?.message || 'Failed to fetch user data.');
        }

        const user = responseData.data.user;
        populateUserForm(user);
    } else {
        const response = await fetch(`${BASE_URL}/api/users`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('Failed to fetch user data.');
        }

        const user = await response.json();
        populateUserForm(user);
    }
}

function populateUserForm(user) {
    document.getElementById('firstName').value = user.firstName;
    document.getElementById('lastName').value = user.lastName;
    document.getElementById('birthDate').value = user.birthDate;

    populateCountries(user.country.id);
    populateCities(user.country.id, user.city.id);
}

async function populateCountries(selectedCountryId) {
    try {
        const response = await fetch(`${BASE_URL}/auth/country-list`);
        const countries = await response.json();

        const countrySelect = document.getElementById('country');
        countrySelect.innerHTML = '<option value="">Choose Country</option>';

        countries.forEach((country) => {
            const option = document.createElement('option');
            option.value = country.id;
            option.textContent = country.name;
            if (country.id === parseInt(selectedCountryId, 10)) {
                option.selected = true;
            }
            countrySelect.appendChild(option);
        });

        countrySelect.addEventListener('change', () => populateCities(countrySelect.value, null));
    } catch (error) {
        console.error('Error fetching countries:', error);
    }
}

async function populateCities(countryId, selectedCityId) {
    const citySelect = document.getElementById('city');
    if (!countryId) {
        citySelect.innerHTML = '<option value="">Choose City</option>';
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/auth/${countryId}/city-list`);
        const cities = await response.json();

        citySelect.innerHTML = '<option value="">Choose City</option>';
        cities.forEach((city) => {
            const option = document.createElement('option');
            option.value = city.id;
            option.textContent = city.name;
            if (city.id === parseInt(selectedCityId, 10)) {
                option.selected = true;
            }
            citySelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching cities:', error);
        citySelect.innerHTML = '<option value="">Choose City</option>';
    }
}

function handleFieldErrors(errors) {
    clearFieldErrors();

    errors.forEach((error) => {
        if (error.message.includes('User must be at least 18 years old')) {
            displayFieldError('birthDate', 'User must be at least 18 years old');
        } else {
            errorMessage.textContent = error.message || 'An error occurred.';
            errorMessage.style.display = 'block';
        }
    });
}

function displayFieldError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorSpan = document.createElement('span');
    errorSpan.className = 'field-error';
    errorSpan.style.color = 'red';
    errorSpan.style.fontSize = '12px';
    errorSpan.style.marginTop = '5px';
    errorSpan.style.display = 'block';
    errorSpan.textContent = message;
    field.parentElement.appendChild(errorSpan);
}

function clearFieldErrors() {
    document.querySelectorAll('.field-error').forEach((errorSpan) => errorSpan.remove());
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.style.display = 'none';
}