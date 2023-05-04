package ru.memorygame.world.words.data.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.memorygame.world.words.R
import ru.memorygame.world.words.data.game.model.CardItem

class Game {

    private var _cardList = MutableStateFlow(getCardList())
    val cardList = _cardList.asStateFlow()

    private val _attempts = MutableStateFlow(0)
    var attempts = _attempts.asStateFlow()
    
    fun restartGame() {
        _cardList.value = getCardList()
        _attempts.value = 0
    }

    fun finisGame():Boolean {
        return _cardList.value.size == 0
    }

    fun isSimilarCard(first: CardItem, second: CardItem): Boolean {
        val isSimilarCard = first.id == second.id

        if(isSimilarCard){
            var firstIndex = 0
            var secondIndex = 0
            _cardList.value.filterIndexed { index, it ->
                val filter =it.id == first.id
                if(filter){
                    firstIndex = index
                }
                filter
            }

            _cardList.value.filterIndexed { index, it ->
                val filter =it.id == first.id
                if(filter){
                    secondIndex = index
                }
                filter
            }

            _cardList.value[firstIndex] = _cardList.value[firstIndex].copy(visibility = false)
            _cardList.value[secondIndex] = _cardList.value[secondIndex].copy(visibility = false)
        }

        _attempts.value++
        return isSimilarCard
    }

    fun updateCardList(): List<CardItem> {
        _cardList.value = getCardList()
        return getCardList()
    }

    private fun getCardList(): MutableList<CardItem> {
        return arrayListOf(
            CardItem(
                id = 1,
                icon = R.drawable.a1,
            ),
            CardItem(
                id = 2,
                icon = R.drawable.a2,
            ),
            CardItem(
                id = 3,
                icon = R.drawable.a3,
            ),
            CardItem(
                id = 4,
                icon = R.drawable.a4,
            ),
            CardItem(
                id = 5,
                icon = R.drawable.a5,
            ),
            CardItem(
                id = 6,
                icon = R.drawable.a6,
            ),
            CardItem(
                id = 7,
                icon = R.drawable.a7,
            ),
            CardItem(
                id = 8,
                icon = R.drawable.a8,
            ),
            CardItem(
                id = 9,
                icon = R.drawable.a9,
            ),
            CardItem(
                id = 10,
                icon = R.drawable.a10,
            ),
            CardItem(
                id = 11,
                icon = R.drawable.a11,
            ),
            CardItem(
                id = 12,
                icon = R.drawable.a12,
            ),
            CardItem(
                id = 13,
                icon = R.drawable.a13,
            ),
            CardItem(
                id = 14,
                icon = R.drawable.a14,
            ),
            CardItem(
                id = 15,
                icon = R.drawable.a15,
            ),
            CardItem(
                id = 16,
                icon = R.drawable.a16,
            ),
        ).shuffled().toMutableList()
    }
}