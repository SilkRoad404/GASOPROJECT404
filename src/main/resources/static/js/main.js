// Filtro de tabla
function filtrar() {
    const texto = document.getElementById('buscador').value.toLowerCase();
    document.querySelectorAll('#tablaBody tr').forEach(fila => {
        fila.style.display = fila.textContent.toLowerCase().includes(texto) ? '' : 'none';
    });
}

// Toggle mostrar/ocultar contraseña
function togglePw(id, btn) {
    const inp = document.getElementById(id);
    if (inp.type === 'password') {
        inp.type = 'text';
        btn.innerHTML = '&#128683;';
    } else {
        inp.type = 'password';
        btn.innerHTML = '&#128065;';
    }
}

// Validar contraseñas
function validar() {
    const p1 = document.getElementById('pass1');
    const p2 = document.getElementById('pass2');
    const err = document.getElementById('passError');
    if (p1 && p2 && p1.value !== p2.value) {
        if (err) err.style.display = 'block';
        return false;
    }
    if (err) err.style.display = 'none';
    return true;
}

// Reloj
function actualizarReloj() {
    const el = document.getElementById('reloj');
    if (!el) return;
    const opciones = { weekday: 'short', year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
    el.textContent = new Date().toLocaleDateString('es-ES', opciones);
}
if (document.getElementById('reloj')) {
    actualizarReloj();
    setInterval(actualizarReloj, 1000);
}






