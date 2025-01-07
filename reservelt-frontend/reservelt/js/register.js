const BASE_URL = "http://localhost:8080";

async function fetchCountries() {
    try {
        const response = await fetch(`${BASE_URL}/auth/country-list`);
        const countries = await response.json();

        const countrySelect = document.getElementById('country');
        countrySelect.innerHTML = '<option value="">Choose Country</option>';

        countries.forEach(country => {
            const option = document.createElement('option');
            option.value = country.id;
            option.textContent = country.name;
            countrySelect.appendChild(option);
        });

        const selectedCountry = countrySelect.value;
        if (selectedCountry) {
            await populateCities(selectedCountry);
        }
    } catch (error) {
        console.error('Error fetching countries:', error);
    }
}

async function populateCities(countryId) {
    try {
        const citySelect = document.getElementById('city');
        citySelect.innerHTML = '<option value="">Choose City</option>';

        const response = await fetch(`${BASE_URL}/auth/${countryId}/city-list`);
        const cities = await response.json();

        cities.forEach(city => {
            const option = document.createElement('option');
            option.value = city.id;
            option.textContent = city.name;
            citySelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching cities:', error);
    }
}

function isGraphQL() {
    return window.location.href.includes('graphql');
}

async function handleFormSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const jsonData = Object.fromEntries(formData.entries());

    if (isGraphQL()) {
        jsonData.country = parseInt(jsonData.country, 10);
        jsonData.city = parseInt(jsonData.city, 10);

        try {
            const response = await fetch(`${BASE_URL}/graphql`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    query: `
                    mutation($userRegisterDto: UserRegisterDto!) {
                        register_user(userRegisterDto: $userRegisterDto) {
                            id
                        }
                    }`,
                    variables: { userRegisterDto: jsonData },
                }),
            });

            const data = await response.json();

            if (data.errors) {
                const firstErrorMessage = data.errors?.[0]?.message || "Login failed!";
                const cleanErrorMessage = firstErrorMessage.replace('Invalid input: ', '').trim();
                handleFieldErrors(cleanErrorMessage);
                return;
            }

            if (data.data?.register_user?.id) {
                localStorage.setItem('successMessage', 'Successfully Registered!');
                window.location.href = 'login.html';
            } else {
                displayGeneralError('Registration failed! Please check the details.');
            }
        } catch (error) {
            displayGeneralError(error.message || 'Registration failed!');
            console.error('Error during registration:', error);
        }
    } else {
        try {
            const response = await fetch(`${BASE_URL}/api/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(jsonData),
            });

            const data = await response.json();

            if (response.ok && data.id) {
                localStorage.setItem('successMessage', 'Successfully Registered!');
                window.location.href = 'login.html';
            } else {
                handleFieldErrors([{ message: data.message }]);
            }
        } catch (error) {
            displayGeneralError(error.message || 'Registration failed!');
            console.error('Error during registration:', error);
        }
    }
}

function handleFieldErrors(errors) {
    clearFieldErrors();

    errors.forEach((error) => {
        if (error.message.includes('User must be at least 18 years old')) {
            displayFieldError('birthDate', error.message);
        } else if (error.message.includes('Password and password confirmation do not match')) {
            displayFieldError('passwordConfirmation', error.message);
        } else if (error.message.includes('Email is already taken')) {
            displayFieldError('email', error.message);
        } else if (error.message.includes('Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)')) {
            displayFieldError('password', error.message);
        } else {
            displayGeneralError(error.message);
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

function displayGeneralError(message) {
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
}

function clearFieldErrors() {
    document.querySelectorAll('.field-error').forEach((errorSpan) => errorSpan.remove());
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.style.display = 'none';
}

document.addEventListener('DOMContentLoaded', fetchCountries);

document.getElementById('country').addEventListener('change', function () {
    const countryId = this.value;
    populateCities(countryId);
});

document.getElementById('registerForm').addEventListener('submit', handleFormSubmit);
