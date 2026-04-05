/**
 * Genera una alerta flat que flota sobre el contenido
 */
function showAlert(message, type = 'info', containerSelector = 'body') {
    const alertDiv = document.createElement('div');
    // Mantenemos tus clases originales + una clase nueva 'alert-floating'
    alertDiv.className = `alert alert-${type} alert-floating`;

    alertDiv.innerHTML = `
        <div style="display: flex; align-items: center; gap: 12px; width: 100%;">
            <span>${message}</span>
            <button type="button" class="alert-close" aria-label="Cerrar" style="margin-left: auto;">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="18" y1="6" x2="6" y2="18"></line>
                    <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
            </button>
        </div>
    `;

    // Lo insertamos siempre en el body para que la posición fija sea respecto a la pantalla
    document.body.appendChild(alertDiv);

    // Evento para cerrar
    const closeBtn = alertDiv.querySelector('.alert-close');
    closeBtn.onclick = () => {
        alertDiv.style.opacity = '0';
        setTimeout(() => alertDiv.remove(), 300);
    };

    // Auto-cerrado.....
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.style.opacity = '0';
            setTimeout(() => alertDiv.remove(), 300);
        }
    }, 5000);
}

window.showAlert = showAlert;