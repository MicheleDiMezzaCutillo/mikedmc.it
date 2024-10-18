document.addEventListener('DOMContentLoaded', () => {
    const checkbox = document.getElementById('checkbox');

    // Recupera la preferenza del tema salvata nel localStorage
    const savedTheme = localStorage.getItem('theme');

    // Imposta il tema iniziale basato sulla preferenza salvata
    if (savedTheme) {
        document.documentElement.setAttribute('data-theme', savedTheme);
        checkbox.checked = savedTheme === 'light'; // Sincronizza lo stato della checkbox con il tema
    } else {
        // Imposta un tema predefinito se non c'è preferenza salvata
        document.documentElement.setAttribute('data-theme', 'dark');
        checkbox.checked = false; // Tema scuro per default
    }

    // Funzione per cambiare il tema e salvare la preferenza
    function changeTheme(newTheme) {
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme); // Salva la preferenza nel localStorage
    }

    // Aggiungi un evento di change alla checkbox per cambiare il tema
    checkbox.addEventListener('change', () => {
        const newTheme = checkbox.checked ? 'light' : 'dark';
        changeTheme(newTheme);
    });
});

// Cambio lingua
// Funzione per aggiornare l'URL con il parametro lang, rimuovendo gli altri parametri
function updateLanguage(event) {
    var lang = event.target.value; // Ottiene il valore selezionato
    var baseUrl = window.location.origin + window.location.pathname; // Ottiene l'URL di base senza parametri
    var newUrl = baseUrl + '?lang=' + lang; // Aggiunge solo il parametro lang
    window.location.href = newUrl; // Ricarica la pagina con il nuovo URL
}


    // Funzione per impostare il valore corretto nel selettore
function setLanguage() {
    var params = new URLSearchParams(window.location.search);
    var lang = params.get('lang') || 'it'; // Imposta la lingua predefinita se non è presente
    var selectElement = document.getElementById('lang-select');
    selectElement.value = lang; // Imposta il valore selezionato
}

// Imposta la lingua al caricamento della pagina
window.onload = setLanguage;

document.addEventListener('DOMContentLoaded', function() {
    const popupItem = document.querySelector('.popup-item');
    const closeButton = document.querySelector('.popup .close');

    // Function to hide the popup
    function hidePopup() {
        popupItem.classList.remove('show');
    }

    // Hide popup when close button is clicked
    if (closeButton) {
        closeButton.addEventListener('click', hidePopup);
    }
});