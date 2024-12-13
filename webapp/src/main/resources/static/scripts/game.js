/* Updated game.js */

const pieceMap = {
    'R': '\u2656', 'N': '\u2658', 'B': '\u2657', 'Q': '\u2655', 'K': '\u2654', 'P': '\u2659',
    'r': '\u265C', 'n': '\u265E', 'b': '\u265D', 'q': '\u265B', 'k': '\u265A', 'p': '\u265F'
};

const colorMap = {
    'W': 'White', 
    'B': 'Black'
};

let theme = 'arialTheme';  // Default theme
let selectedSquare = null; // Track selected square

// Initialize the game when the page loads
function bodyLoaded() {
    console.log("Body loaded");
    renderBoard(); // Ensure board is rendered
    requestUpdatedBoard();
    requestCurrentPlayer();
}

// Update theme when radio buttons are clicked
function updateTheme(themeId) {
    theme = themeId;
    // Re-render the board with new theme
    requestUpdatedBoard();
}

// Render the chessboard squares dynamically
function renderBoard() {
    const boardGroup = document.getElementById('board');
    boardGroup.innerHTML = ''; // Clear existing squares

    // Create squares
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const square = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
            square.setAttribute('x', col * 60);  // Reduced size
            square.setAttribute('y', row * 60);  // Reduced size
            square.setAttribute('width', 60);    // Reduced size
            square.setAttribute('height', 60);   // Reduced size
            square.setAttribute('fill', (row + col) % 2 === 0 ? '#f0d9b5' : '#b58863');
            square.setAttribute('id', `${String.fromCharCode(97 + col)}${8 - row}`);
            square.addEventListener('click', () => handleSquareClick(square.id));
            boardGroup.appendChild(square);
        }
    }
}

// Handle square clicks
function handleSquareClick(squareId) {
    if (selectedSquare === null) {
        // First click - select the square
        selectedSquare = squareId;
        highlightSquare(squareId, 'rgba(0, 255, 0, 0.3)'); // Green highlight for selected square
    } else {
        // Second click - attempt to move
        sendPolygonClicked(selectedSquare + squareId);
        unhighlightSquare(selectedSquare);
        selectedSquare = null;
    }
}

// Highlight a square
function highlightSquare(squareId, color) {
    const square = document.getElementById(squareId);
    if (square) {
        square.setAttribute('fill', color);
    }
}

// Unhighlight a square
function unhighlightSquare(squareId) {
    const square = document.getElementById(squareId);
    if (square) {
        const row = 8 - parseInt(squareId[1]);
        const col = squareId.charCodeAt(0) - 97;
        square.setAttribute('fill', (row + col) % 2 === 0 ? '#f0d9b5' : '#b58863');
    }
}

// Update the current player based on the server response
function updateCurrentPlayer(color) {
    const colourName = colorMap[color] || 'Unknown';
    const playerName = localStorage.getItem(colourName);

    const p_name = document.getElementById('pl-name');
    p_name.textContent = playerName || 'Unknown';

    const p_colour = document.getElementById('pl-colour');
    p_colour.style.color = getColorForDisplay(color);
}

// Get display color for pieces
function getColorForDisplay(color) {
    switch(color) {
        case 'W': return '#FFFFFF';
        case 'B': return '#000000';
        default: return '#000000';
    }
}

// Update the board after a response from the server
function updateBoard(response) {
    clearBoard();
    console.log('New Board Configuration:', response);

    const board = response['board'];
    const highlightedPolygons = response['highlightedPolygons'];
    const winner = response['winner'];

    if (response['gameOver']) {
        showGameOverPopup(winner);
    }

    if (board) {
        console.log('Updating pieces with board:', board);
        updatePieces(board);
    }

    if (highlightedPolygons) {
        displayPossibleMoves(highlightedPolygons);
    }
}

// Parse position string that may include color prefix
function parsePosition(posStr) {
    // Remove color prefix if present (e.g., "Rc2" -> "c2" or "Ba7" -> "a7")
    const match = posStr.match(/[RB]?([a-h][1-8])/);
    if (!match) {
        console.error(`Invalid position string: ${posStr}`);
        return null;
    }
    return match[1];  // Return the position part without the color prefix
}

// Render the pieces on the board
function updatePieces(board) {
    const piecesGroup = document.getElementById('pieces');
    if (!piecesGroup) {
        console.error('Pieces group not found');
        return;
    }
    piecesGroup.innerHTML = '';

    for (const pos in board) {
        const value = board[pos];
        console.log(`Processing piece at ${pos}: ${value}`);
        
        if (!value || value.length < 2) {
            console.error(`Invalid piece value at ${pos}: ${value}`);
            continue;
        }

        // Convert R to W for white pieces
        const pieceColor = pos.startsWith('R') ? 'W' : (pos.startsWith('B') ? 'B' : value[0]);
        const pieceToken = value[1];

        // Parse position with color prefix
        const standardPos = parsePosition(pos);
        if (!standardPos) continue;

        // Parse position (e.g., "a1", "e4", etc.)
        const col = standardPos.charCodeAt(0) - 97;  // 'a' -> 0, 'b' -> 1, etc.
        const row = 8 - parseInt(standardPos[1]);    // '1' -> 7, '2' -> 6, etc.

        if (isNaN(col) || isNaN(row)) {
            console.error(`Invalid position: ${standardPos}`);
            continue;
        }

        const x = col * 60 + 30;  // Center in square
        const y = row * 60 + 30;  // Center in square

        const textElement = getPieceText(x, y, pieceColor, pieceToken);
        piecesGroup.appendChild(textElement);
    }
}

// Helper function to create a text element for a piece
function getPieceText(x, y, color, pieceToken) {
    const textElement = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    textElement.setAttribute('x', x);
    textElement.setAttribute('y', y);
    textElement.setAttribute('text-anchor', 'middle');
    textElement.setAttribute('dominant-baseline', 'middle');
    textElement.setAttribute('fill', getColorForDisplay(color));
    textElement.setAttribute('font-size', '40');  // Adjusted size
    textElement.setAttribute('font-weight', 'bold');
    textElement.setAttribute('stroke', color === 'W' ? '#000000' : '#FFFFFF');
    textElement.setAttribute('stroke-width', '0.5');
    textElement.setAttribute('class', theme);

    const unicode = pieceMap[pieceToken];
    textElement.textContent = unicode;
    console.log(`Rendering piece: ${pieceToken} (${unicode}) at (${x}, ${y}) with color ${colorMap[color]}`);

    return textElement;
}

// Clear all pieces from the board
function clearBoard() {
    const piecesGroup = document.getElementById('pieces');
    if (piecesGroup) {
        piecesGroup.innerHTML = '';
    }
}

// Send the clicked polygon (square) to the server for processing
function sendPolygonClicked(polygonId) {
    const request = new XMLHttpRequest();
    request.open("POST", "/onClick", false);
    request.setRequestHeader('Content-Type', 'text/plain');
    request.send(polygonId);

    if (request.status === 200) {
        const data = JSON.parse(request.response);
        updateBoard(data);
    }
}

// Request the updated board from the server
function requestUpdatedBoard() {
    console.log("Request Current Board");
    const request = new XMLHttpRequest();
    request.open("GET", "/board", false);
    request.send(null);

    if (request.status === 200) {
        try {
            const data = JSON.parse(request.response);
            console.log("Received board data:", data);
            updateBoard({ board: data });
        } catch (e) {
            console.error("Error parsing board data:", e);
        }
    }
}

// Request the current player from the server
function requestCurrentPlayer() {
    const request = new XMLHttpRequest();
    request.open("GET", "/currentPlayer", false);
    request.send(null);

    if (request.status === 200) {
        const player = request.response;
        updateCurrentPlayer(player);
    }
}

// Show a game over popup when the game ends
function showGameOverPopup(winner) {
    const colourName = colorMap[winner] || 'Unknown';
    const playerName = localStorage.getItem(colourName);
    document.getElementById('popup').style.display = 'block';
    const winnerText = `${playerName} (${colourName}) has won the Game!`;
    document.getElementById('winner').innerText = winnerText;
}

// Close the game over popup
function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

// Display possible moves for a piece (highlighted squares)
function displayPossibleMoves(highlightedPolygons) {
    if (!highlightedPolygons) return;
    
    highlightedPolygons.forEach(polygonId => {
        const square = document.getElementById(polygonId);
        if (square) {
            square.setAttribute('fill', 'rgba(255, 255, 0, 0.5)');
        }
    });
}
