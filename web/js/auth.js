// js/auth.js

/**
 * Objeto global para manejar la autenticación en Sifenboot
 */
const Auth = {
    async logout() {
        try {
            // Mostramos feedback visual si showAlert está disponible
            if (typeof showAlert === 'function') {
                showAlert('Cerrando sesión...', 'info');
            }

            const response = await fetch('/app/auth/logout', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });

            if (response.ok || response.status === 204) {
                // Limpieza local y redirección
                localStorage.removeItem('token'); // O 'user_data' según uses
                window.location.href = '/login';
            } else {
                throw new Error('Error en la respuesta del servidor');
            }
        } catch (error) {
            console.error("Error al cerrar sesión:", error);
            // Fallback: si el servidor falla, igual sacamos al usuario por seguridad
            window.location.href = '/login';
        }
    }
};

// Lo exponemos globalmente para que cualquier página lo vea
window.Auth = Auth;