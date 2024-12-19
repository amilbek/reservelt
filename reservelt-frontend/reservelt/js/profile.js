const BASE_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", async () => {
    const successMessage = localStorage.getItem("successMessage");
    if (successMessage) {
        const messageElement = document.getElementById("successMessage");
        messageElement.textContent = successMessage;
        messageElement.style.display = "block";
        localStorage.removeItem("successMessage");
    }

    const token = localStorage.getItem("authToken");

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    const errorMessage = document.getElementById("errorMessage");
    const userProfile = document.getElementById("userProfile");

    errorMessage.textContent = "";

    function isGraphQL() {
        return window.location.href.includes("graphql");
    }

    try {
        const user = isGraphQL()
            ? await fetchGraphQLUserProfile(token)
            : await fetchRestUserProfile(token);

        if (user) {
            document.getElementById("greeting").textContent = `Welcome, ${user.firstName}!`;
            document.getElementById("email").textContent = user.email;
            document.getElementById("firstName").textContent = user.firstName;
            document.getElementById("lastName").textContent = user.lastName;
            document.getElementById("birthDate").textContent = user.birthDate;
            document.getElementById("country").textContent = user.country.name;
            document.getElementById("city").textContent = user.city.name;
            userProfile.style.display = "block";
        }
    } catch (error) {
        console.error("Error fetching profile:", error);
        errorMessage.textContent = error.message || "An error occurred while loading your profile.";
    }

    document.getElementById("logoutButton").addEventListener("click", () => {
        localStorage.removeItem("authToken");
        localStorage.setItem("successMessage", "You have been logged out successfully!");
        window.location.href = "login.html";
    });

    document.getElementById("openDeleteModalBtn").addEventListener("click", () => {
        document.getElementById("deleteAccountModal").style.display = "block";
    });

    document.getElementById("cancelDeleteBtn").addEventListener("click", () => {
        document.getElementById("deleteAccountModal").style.display = "none";
    });

    document.getElementById("confirmDeleteBtn").addEventListener("click", async () => {
        if (!token) {
            window.location.href = "login.html";
            return;
        }

        try {
            if (isGraphQL()) {
                await deleteGraphQLAccount(token);
            } else {
                await deleteRestAccount(token);
            }

            localStorage.removeItem("authToken");
            localStorage.setItem("successMessage", "Your account has been deleted successfully!");
            window.location.href = "login.html";
        } catch (error) {
            console.error("Error deleting account:", error);
            errorMessage.textContent = error.message || "An error occurred while deleting your account.";
            errorMessage.style.display = "block";
        }
    });
});

async function fetchGraphQLUserProfile(token) {
    const response = await fetch(`${BASE_URL}/graphql`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
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
                            name
                        }
                        city {
                            name
                        }
                    }
                }
            `,
        }),
    });

    const data = await response.json();
    if (data.errors) {
        if (data.errors[0].message.includes("Unauthorized")) {
            throw new Error("Session expired. Please log in again.");
        }
        throw new Error(data.errors[0].message);
    }

    return data.data?.user;
}

async function fetchRestUserProfile(token) {
    const response = await fetch(`${BASE_URL}/api/users`, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        },
        credentials: "include",
    });

    if (!response.ok) {
        if (response.status === 401) {
            throw new Error("Session expired. Please log in again.");
        }
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
}

async function deleteGraphQLAccount(token) {
    const response = await fetch(`${BASE_URL}/graphql`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            query: `
                mutation {
                    delete_user
                }
            `,
        }),
    });

    const data = await response.json();
    if (data.errors) {
        throw new Error(data.errors[0].message);
    }
}

async function deleteRestAccount(token) {
    const response = await fetch(`${BASE_URL}/api/users/delete`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error("Failed to delete account. Please try again.");
    }
}
