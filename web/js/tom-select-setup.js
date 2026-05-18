    document.addEventListener('DOMContentLoaded', () => {


        // 1. Configuración base para Tom Select
        // Agregamos un hook 'onChange' para disparar el evento nativo 'change'.
        // Esto es CRUCIAL para que HTMX detecte el cambio en las cascadas.
        const tsConfig = {
            create: false,
            sortField: { field: "text", direction: "asc" },
            plugins: ['dropdown_input'],
            onChange: function(value) {
                // Disparamos el evento change nativo en el <select> original
                // para que hx-get="/..." de HTMX se active
                this.input.dispatchEvent(new Event('change', { bubbles: true }));
            }
        };

        // 2. Función para inicializar solo si no existe
        function initTomSelects(container) {
            if (!container) return;
            container.querySelectorAll('.tom-select').forEach(select => {
                // Solo inicializamos si no tiene instancia y no está disabled
                // (Si quieres que aplique estilo aunque esté disabled, quita la condición !select.disabled)
                if (!select.tomselect && !select.disabled) {
                    new TomSelect(select, tsConfig);
                }
            });
        }

        // 3. Carga inicial
        initTomSelects(document);

        // 4. Escucha de eventos HTMX

        // A. Antes de hacer el swap (beforeSwap)
        // Ya NO destruimos los elementos aquí.
        // Dejamos que HTMX actualice el HTML interno (<option>).
        // Esto evita el parpadeo del control visual.

        // B. Después de asentar el HTML (afterSettle)
        document.body.addEventListener('htmx:afterSettle', (evt) => {
            const target = evt.detail.target;
            if (!target) return;

            // CASO 1: Si el objetivo es un <select> con Tom Select (ej: actualización de Distrito/Localidad)
            if (target.classList.contains('tom-select') && target.tomselect) {
                // ¡Magia aquí!
                // Sincronizamos las nuevas opciones que HTMX puso en el DOM con el widget visual.
                target.tomselect.sync();

                // Opcional: Si al sincronizar el valor anterior ya no existe,
                // Tom Select se pone vacío. Si quieres forzar la limpieza visual:
                // target.tomselect.setValue('');
            }

            // CASO 2: Si el objetivo es un contenedor (ej: se cargó todo el formulario nuevo)
            if (target.id === 'main-content' || target.classList.contains('form-container')) {
                initTomSelects(target);
            }
        });
    });
