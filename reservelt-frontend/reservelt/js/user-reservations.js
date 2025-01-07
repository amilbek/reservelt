async function fetchReservations() {
    try {
        const response = await fetch('/api/reservations/my-reservations');
        if (!response.ok) throw new Error('Failed to fetch reservations');

        const reservations = await response.json();

        if (reservations.length === 0) {
            document.getElementById('no-reservations').style.display = 'block';
            return;
        }

        document.getElementById('reservations-table').style.display = 'block';
        const tbody = document.getElementById('reservations-body');
        tbody.innerHTML = ''; // Clear existing rows

        reservations.forEach((reservation, index) => {
            const row = document.createElement('tr');

            // Row cells
            row.innerHTML = `
          <td>${index + 1}</td>
          <td>${reservation.restaurantName}</td>
          <td>${getReadableStatus(reservation.tableReservationStatus)}</td>
          <td>${getActionButtons(reservation)}</td>
        `;

            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Error fetching reservations:', error);
        alert('Failed to load reservations. Please try again later.');
    }
}

function getReadableStatus(status) {
    switch (status) {
        case 'RESERVED': return 'Reserved';
        case 'IN_PROCESS': return 'In Process';
        case 'COMPLETED': return 'Completed';
        case 'CANCELLED': return 'Cancelled';
        default: return 'Unknown';
    }
}

function getActionButtons(reservation) {
    const { id, tableReservationStatus } = reservation;

    if (tableReservationStatus === 'RESERVED') {
        return `
        <form onsubmit="changeStatus(event, ${id}, 'IN_PROCESS')" style="display: inline-block;">
          <button type="submit" class="btn btn-primary btn-sm">Mark as In Process</button>
        </form>
        <form onsubmit="changeStatus(event, ${id}, 'CANCELLED')" style="display: inline-block;">
          <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
        </form>
      `;
    }

    if (tableReservationStatus === 'IN_PROCESS') {
        return `
        <form onsubmit="changeStatus(event, ${id}, 'COMPLETED')" style="display: inline-block;">
          <button type="submit" class="btn btn-success btn-sm">Mark as Completed</button>
        </form>
      `;
    }

    if (tableReservationStatus === 'COMPLETED' || tableReservationStatus === 'CANCELLED') {
        return `<span class="text-muted">No further actions</span>`;
    }

    return '';
}

async function changeStatus(event, reservationId, newStatus) {
    event.preventDefault();

    try {
        const response = await fetch('/api/reservations/change-status', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `reservationId=${reservationId}&newStatus=${newStatus}`
        });

        if (!response.ok) throw new Error('Failed to change status');

        alert('Reservation status updated successfully!');
        fetchReservations(); // Reload reservations
    } catch (error) {
        console.error('Error changing status:', error);
        alert('Failed to update reservation status. Please try again later.');
    }
}
fetchReservations();