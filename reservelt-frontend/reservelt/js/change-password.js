const BASE_URL = "http://localhost:8080";

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('authToken');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    function isGraphQL() {
        return window.location.href.includes('graphql');
    }

    document.getElementById('changePasswordForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const currentPassword = document.getElementById('current_password').value;
        const newPassword = document.getElementById('new_password').value;
        const newPasswordConfirmation = document.getElementById('new_password_confirmation').value;

        clearFieldErrors();

        const data = {
            currentPassword: currentPassword,
            newPassword: newPassword,
            newPasswordConfirmation: newPasswordConfirmation,
        };

        try {
            if (isGraphQL()) {
                await changePasswordGraphQL(data, token);
            } else {
                await changePasswordREST(data, token);
            }

            localStorage.setItem('successMessage', 'Password changed successfully!');
            window.location.href = 'profile.html';
        } catch (error) {
            handleFieldErrors(error.message);
        }
    });

    document.getElementById("logoutButton").addEventListener("click", () => {
        localStorage.removeItem("authToken");
        localStorage.setItem("successMessage", "You have been logged out successfully!");
        window.location.href = "login.html";
    });

    async function changePasswordGraphQL(data, token) {
        const response = await fetch(`${BASE_URL}/graphql`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                query: `
                    mutation changePassword($changeUserPasswordDto: ChangeUserPasswordDto!) {
                        change_password(changeUserPasswordDto: $changeUserPasswordDto)
                    }
                `,
                variables: { changeUserPasswordDto: data },
            }),
        });

        const responseData = await response.json();

        if (responseData.errors) {
            const firstErrorMessage = responseData.errors[0].message;
            throw new Error(firstErrorMessage.replace('Invalid input: ', '').trim());
        }
    }

    async function changePasswordREST(data, token) {
        const response = await fetch(`${BASE_URL}/api/users/change-password`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to change password.');
        }
    }

    function handleFieldErrors(errorMessage) {
        if (errorMessage.includes('Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)')) {
            displayFieldError('new_password', errorMessage);
        } else if (errorMessage.includes('Current password does not match')) {
            displayFieldError('current_password', errorMessage);
        } else if (errorMessage.includes('New password and password confirmation do not match')) {
            displayFieldError('new_password_confirmation', errorMessage);
        } else {
            const errorMessageElement = document.getElementById('errorMessage');
            errorMessageElement.textContent = errorMessage || 'An error occurred.';
            errorMessageElement.style.display = 'block';
        }
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
        const errorMessageElement = document.getElementById('errorMessage');
        errorMessageElement.style.display = 'none';
    }
});
