// PEOPLE COUNTER PROGRAM
/*

// DOM: Document Object Model
    // Document = HTML document
    // Object = document keyword is of type object
    // Model = representation: code to elemnt

// javascript
//document.getElementById("count-el").innerText = 5

let count = parseInt( document.getElementById("count-el").innerText )
let prevEl = document.getElementById("prev-entries")

function increment() {
    count += 1
    document.getElementById("count-el").textContent = count
}

function save() {
    console.log(count)
    prevEl.textContent += count + " - "
    count = 0
    document.getElementById("count-el").textContent = count
}
*/


/*
let num1 = 8
let num2 = 2
document.getElementById("num1-el").textContent = num1
document.getElementById("num2-el").textContent = num2
let result, sumEl = document.getElementById("sum-el")

function add() {
    result = num1 + num2
    sumEl.textContent = "Sum: " + result
}
function subtract() {
    result = num1 - num2
    sumEl.textContent = "Sum: " + result
}
function multiply() {
   result = num1 * num2
   sumEl.textContent = "Sum: " + result
}
function divide() {
    result = num1 / num2
    sumEl.textContent = "Sum: " + result
}
*/


let player = {
    Name: "Eric",
    Chips: 145
}
let playerEl = document.getElementById("player-el")
playerEl.textContent = player.Name + ": $" + player.Chips

let cards, sum = 0  
let hasBlackjack = false, isAlive = true
let message = ""
let messageEl = document.getElementById("message-el")
let sumEl = document.querySelector("#sum-el") //document.getElementById("sum-el")
let cardsEl = document.querySelector("#cards-el")

function getRandomCard() {
    let randomCard = Math.floor( Math.random() * 13 ) + 1
    if (randomCard == 1) {
        return 11
    } else if (randomCard > 10) {
        return 10
    }
    return randomCard
}

function startGame() {
    isAlive = true, hasBlackjack = false
    cards = [getRandomCard(), getRandomCard()]
    sum = cards[0] + cards[1]
    renderGame()
    
}

function renderGame() {

    sumEl.textContent = "Sum: " + sum
    cardsEl.textContent = "Cards: " 
    for (x= 0; x < cards.length; x++)
    {
        cardsEl.textContent += cards[x] + " "
    }

    if (sum <= 20) {
        message = "Do you want to draw a new card?"
    }
    else if (sum === 21) {
        message = "Wohoo! You've got Blackjack!"
        hasBlackjack = true
    }
    else {
        message = "You're out of the game!"
        isAlive = false
    }

    // CashOut
    if (hasBlackjack) {
        console.log(message)
    }
    messageEl.textContent = message
}

function newCard() {
    if(isAlive &&  !hasBlackjack) {
        let newCard = getRandomCard()
        sum += newCard
        cards.push(newCard)
        renderGame()
        //cardsEl.textContent += " " + newCard
    }
}

let hands = ["rock", "paper","scissor"]
function getHand() {return hands[Math.floor(Math.random() * 3)] }
console.log(getHand())



