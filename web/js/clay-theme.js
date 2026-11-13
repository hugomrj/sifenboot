/* ============================================================
   clay-theme.js  ·  v1.5 (Sifenboot · multi-tenant)
   ------------------------------------------------------------
   Gestiona:
     - Modo (light/dark)
     - Paleta (teal, blue, grey, rose, amber, violet)
     - Persistencia en localStorage
     - Default del tenant (fallback cuando el user no eligió)
     - Switcher del header (modo + menú de paletas)

   API global:
     clayTheme.init(tenantStyle?)
     clayTheme.apply(theme, style)
     clayTheme.toggleMode()
     clayTheme.setStyle(style)
     clayTheme.setTenantDefault(style)
     clayTheme.resetToTenantDefault()
     clayTheme.currentTheme()
     clayTheme.currentStyle()

   Uso desde Thymeleaf:
     <script th:src="@{/js/clay-theme.js}" defer></script>
     <script>
       document.addEventListener('DOMContentLoaded', function () {
         var tenantStyle = document.documentElement.dataset.tenantStyle || 'teal';
         clayTheme.init(tenantStyle);
       });
     </script>
   ============================================================ */

(function () {
    'use strict';

    // ============================================================
    // CONSTANTES
    // ============================================================
    const VALID_THEMES = ['light', 'dark'];
    const VALID_STYLES = ['teal', 'blue', 'violet', 'rose', 'amber', 'grey'];

    const STYLE_TO_CLASS = {
        teal:   null,           // teal es el default (sin clase extra)
        blue:   'tx-blue',
        violet: 'tx-violet',
        rose:   'tx-rose',
        amber:  'tx-amber',
        grey:   'tx-grey'
    };

    const STYLE_LABELS = {
        teal:   'Teal Clay',
        blue:   'Blue Soft',
        violet: 'Violet Mist',
        rose:   'Rose Clay',
        amber:  'Amber Sand',
        grey:   'Soft Grey'
    };

    const STYLE_ORDER = ['teal', 'blue', 'violet', 'rose', 'amber', 'grey'];

    const STORAGE_KEYS = {
        theme: 'tx-theme',
        style: 'tx-style'
    };

    // ============================================================
    // ESTADO INTERNO
    // ============================================================
    const root = document.documentElement;
    let _tenantDefault = null;  // paleta por defecto del tenant actual

    // ============================================================
    // HELPERS
    // ============================================================
    function removeAllThemeClasses() {
        root.classList.remove(
            'tx-light', 'tx-dark',
            'tx-blue', 'tx-violet', 'tx-grey', 'tx-rose', 'tx-amber'
        );
    }

    function persistTheme(theme) {
        try { localStorage.setItem(STORAGE_KEYS.theme, theme); } catch (e) {}
    }

    function persistStyle(style) {
        try {
            // Si el style es el mismo que el default del tenant, borramos el override
            if (style === (_tenantDefault || 'teal')) {
                localStorage.removeItem(STORAGE_KEYS.style);
            } else {
                localStorage.setItem(STORAGE_KEYS.style, style);
            }
        } catch (e) {}
    }

    function readStoredTheme() {
        try {
            const t = localStorage.getItem(STORAGE_KEYS.theme);
            return VALID_THEMES.includes(t) ? t : null;
        } catch (e) { return null; }
    }

    function readStoredStyle() {
        try {
            const s = localStorage.getItem(STORAGE_KEYS.style);
            return VALID_STYLES.includes(s) ? s : null;
        } catch (e) { return null; }
    }

    function prefersDarkMode() {
        try {
            return window.matchMedia('(prefers-color-scheme: dark)').matches;
        } catch (e) { return false; }
    }

    // ============================================================
    // APLICAR THEME + STYLE
    // ============================================================
    function apply(theme, style, silent) {
        if (!VALID_THEMES.includes(theme)) theme = 'light';
        if (!VALID_STYLES.includes(style)) style = 'teal';

        // Animación suave en el cambio
        root.classList.add('clay-theming');
        window.setTimeout(function () {
            root.classList.remove('clay-theming');
        }, 320);

        // Aplicar clases
        removeAllThemeClasses();
        root.classList.add('tx-' + theme);
        const styleClass = STYLE_TO_CLASS[style];
        if (styleClass) root.classList.add(styleClass);

        // Persistir
        persistTheme(theme);
        persistStyle(style);

        // Marcar items del menú como activos
        document.querySelectorAll('[data-style]').forEach(function (el) {
            el.classList.toggle('is-active', el.dataset.style === style);
        });

        // Actualizar ícono del botón modo (si existe)
        const qtIcon = document.getElementById('qtIcon');
        if (qtIcon) {
            qtIcon.className = 'fa ' + (theme === 'light' ? 'fa-moon-o' : 'fa-sun-o');
        }

        // Emitir evento para que otros componentes reaccionen
        document.dispatchEvent(new CustomEvent('claytheme:change', {
            detail: {
                theme: theme,
                style: style,
                themeClass: 'tx-' + theme,
                styleClass: styleClass || null
            }
        }));

        return { theme: theme, style: style };
    }

    // ============================================================
    // GETTERS
    // ============================================================
    function currentTheme() {
        return root.classList.contains('tx-dark') ? 'dark' : 'light';
    }

    function currentStyle() {
        if (root.classList.contains('tx-amber'))  return 'amber';
        if (root.classList.contains('tx-rose'))   return 'rose';
        if (root.classList.contains('tx-violet')) return 'violet';
        if (root.classList.contains('tx-grey'))   return 'grey';
        if (root.classList.contains('tx-blue'))   return 'blue';
        return 'teal';
    }

    // ============================================================
    // ACCIONES PÚBLICAS
    // ============================================================
    function toggleMode() {
        return apply(
            currentTheme() === 'light' ? 'dark' : 'light',
            currentStyle()
        );
    }

    function setStyle(style) {
        return apply(currentTheme(), style);
    }

    function setTenantDefault(style) {
        if (VALID_STYLES.includes(style)) {
            _tenantDefault = style;
        }
    }

    function resetToTenantDefault() {
        try { localStorage.removeItem(STORAGE_KEYS.style); } catch (e) {}
        return apply(currentTheme(), _tenantDefault || 'teal');
    }

    // ============================================================
    // INICIALIZACIÓN
    // ============================================================
    function init(tenantStyle) {
        // Guardar default del tenant si viene
        if (tenantStyle && VALID_STYLES.includes(tenantStyle)) {
            _tenantDefault = tenantStyle;
        } else if (!_tenantDefault) {
            // Fallback: leer del atributo data-tenant-style del <html>
            const attr = root.dataset.tenantStyle;
            if (attr && VALID_STYLES.includes(attr)) {
                _tenantDefault = attr;
            }
        }

        // Resolver tema: storage > prefers-color-scheme > light
        let theme = readStoredTheme();
        if (!theme) {
            theme = prefersDarkMode() ? 'dark' : 'light';
        }

        // Resolver style: storage > tenant default > teal
        let style = readStoredStyle();
        if (!style) {
            style = _tenantDefault || 'teal';
        }

        apply(theme, style, true);

        return { theme: theme, style: style };
    }

    // ============================================================
    // BIND DE CONTROLES DEL HEADER (auto-wire)
    // ============================================================
    function bindHeaderControls() {
        // Botón modo
        const btnTheme = document.getElementById('quickTheme');
        if (btnTheme && !btnTheme.dataset.bound) {
            btnTheme.dataset.bound = '1';
            btnTheme.addEventListener('click', function (e) {
                e.preventDefault();
                toggleMode();
            });
        }

        // Botón + menú paletas
        const btnStyle = document.getElementById('quickStyle');
        const menu = document.getElementById('styleMenu');

        if (btnStyle && menu && !btnStyle.dataset.bound) {
            btnStyle.dataset.bound = '1';

            // Abrir/cerrar menú
            btnStyle.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                menu.classList.toggle('is-open');
            });

            // Click en item
            menu.querySelectorAll('[data-style]').forEach(function (item) {
                item.addEventListener('click', function () {
                    setStyle(this.dataset.style);
                    menu.classList.remove('is-open');
                });
            });

            // Botón "usar color del tenant"
            const resetBtn = menu.querySelector('[data-action="reset"]');
            if (resetBtn) {
                resetBtn.addEventListener('click', function () {
                    resetToTenantDefault();
                    menu.classList.remove('is-open');
                });
            }

            // Cerrar al click fuera
            document.addEventListener('click', function (e) {
                if (!menu.contains(e.target) && !btnStyle.contains(e.target)) {
                    menu.classList.remove('is-open');
                }
            });

            // Cerrar con Escape
            document.addEventListener('keydown', function (e) {
                if (e.key === 'Escape') menu.classList.remove('is-open');
            });
        }

        // Atajo de teclado: T = toggle modo
        if (!document.documentElement.dataset.kbBound) {
            document.documentElement.dataset.kbBound = '1';
            document.addEventListener('keydown', function (e) {
                if (e.target.matches('input, textarea, select, [contenteditable]')) return;
                if (e.key === 't' || e.key === 'T') {
                    toggleMode();
                }
            });
        }
    }

    // ============================================================
    // EXPORT GLOBAL
    // ============================================================
    window.clayTheme = {
        // API pública
        init: init,
        apply: apply,
        toggleMode: toggleMode,
        setStyle: setStyle,
        setTenantDefault: setTenantDefault,
        resetToTenantDefault: resetToTenantDefault,
        currentTheme: currentTheme,
        currentStyle: currentStyle,

        // Constantes
        VALID_THEMES: VALID_THEMES,
        VALID_STYLES: VALID_STYLES,
        STYLE_ORDER: STYLE_ORDER,
        STYLE_LABELS: STYLE_LABELS,
        STYLE_TO_CLASS: STYLE_TO_CLASS
    };

    // ============================================================
    // AUTO-BIND al cargar el DOM
    // ============================================================
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', bindHeaderControls);
    } else {
        bindHeaderControls();
    }

})();