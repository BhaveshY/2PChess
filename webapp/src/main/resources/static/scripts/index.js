function checkInputs() {
    const pl1 = document.getElementById('pl1').value;
    const pl2 = document.getElementById('pl2').value;
    const newGameButton = document.getElementById('newgamebtn');

    if (pl1 !== '' && pl2 !== '') {
        newGameButton.removeAttribute('disabled');
    } else {
        newGameButton.setAttribute('disabled', 'true');
    }
}

function newGame(){
    const request = new XMLHttpRequest();
    request.open("GET", "/newGame", false);
    const pl1 = document.getElementById('pl1').value;
    const pl2 = document.getElementById('pl2').value;

    localStorage.setItem('White', pl1);
    localStorage.setItem('Black', pl2);

    request.send(null);

    if (request.status === 200) {
        window.location.href = '/game.html';
    }
}