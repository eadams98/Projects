
class Game {
    constructor() {
        this.AI;
        this.items = ['Rock', 'Paper', 'Scissors']
        this.playerWins = 0
        this.AIWins = 0
    }
    
    shoot( item ) {
        let AI = this.items[Math.floor(Math.random() * 3)]
        console.log('Player: ' + item + ' AI: ' + AI)
        let playerEl = document.getElementById("player-el")
        let AIEl = document.getElementById("ai-el")
        playerEl.textContent = 'Player: ' + item, AIEl.textContent = 'AI: ' + AI
        let winnerEl = document.getElementById("winner-el")
        let message;
        
        // Player Wins
        if (item == 'Paper' && AI == 'Rock') {
            message = 'Player Wins'
            console.log(message)
            winnerEl.textContent = message
            this.playerWins++
        }
        if (item == 'Scissors' && AI == 'Paper') {
            message = 'Player Wins'
            console.log(message)
            winnerEl.textContent = message
            this.playerWins++
        }
        
        if (item == 'Rock' && AI == 'Scissors') {
            message = 'Player Wins'
            console.log(message)
            winnerEl.textContent = message
            this.playerWins++
        }
        
        // AI Wins
        if (AI == 'Paper' && item == 'Rock') {
            message = 'AI Wins'
            console.log(message)
            winnerEl.textContent = message
            this.AIWins++
        }
        if (AI == 'Scissors' && item == 'Paper') {
            message = 'AI Wins'
            console.log(message)
            winnerEl.textContent = message
            this.AIWins++
        }
        
        if (AI == 'Rock' && item == 'Scissors') {
            message = 'AI Wins'
            console.log(message)
            winnerEl.textContent = message
            this.AIWins++
        }
        
        // DRAW
        if (AI === item) {
            message = 'It is a draw'
            console.log(message)
            winnerEl.textContent = message
        }
        
        let talleyEl = document.getElementById('talley-el')
        talleyEl.textContent = 'Player: ' + this.playerWins + ' - AI: ' + this.AIWins
    }
}
//let rockPaperScissors = new Game()
//rockPaperScissors.shoot('Paper')
let rockPaperScissors;
function startGame() {
    let btns = ['rock-btn', 'paper-btn', 'scissors-btn']
    let x;
    for (btnIdx= 0; btnIdx < btns.length; btnIdx++) {
        x  = document.getElementById(btns[btnIdx])
        x.style.visibility = "visible";
    }	
    rockPaperScissors = new Game()
    console.log("Game has started !!")
    let talleyEl = document.getElementById('talley-el')
    let playerEl = document.getElementById("player-el")
    let AIEl = document.getElementById("ai-el")
    let winnerEl = document.getElementById("winner-el")
    playerEl.textContent = '' , AIEl.textContent = '', winnerEl.textContent = ''
    talleyEl.textContent = 'Player: ' + 0 + ' - AI: ' + 0
}

function draw(choice) {
    rockPaperScissors.shoot(choice)
}
