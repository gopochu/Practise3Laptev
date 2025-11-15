package com.vadlap.practise3.news

import androidx.annotation.DrawableRes
import com.vadlap.practise3.R

// 1. Создаем модель данных
data class KinoItem(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int
)

// 2. Создаем фейковый репозиторий с данными
object KinoRepository {
    fun getKinoList(): List<KinoItem> {
        return listOf(
            KinoItem(1, "Гарри Поттер", "Мальчик, который выжил", R.drawable.harry),
            KinoItem(2, "Властелин колец", "Путешествие в Мордор", R.drawable.vlastelin),
            KinoItem(3, "Звездные войны", "Давным-давно, в далекой-далекой галактике...",
                R.drawable.star
            ),
            KinoItem(4, "Матрица", "Что такое Матрица?", R.drawable.matrica),
            KinoItem(5, "Интерстеллар", "Человечество было рождено на Земле. Ему не суждено умереть здесь.",
                R.drawable.interstellar
            ),
            KinoItem(6, "Начало", "Твой разум - место преступления.", R.drawable.nachalo),
            KinoItem(7, "Криминальное чтиво", "Just because you are a character doesn't mean you have character.",
                R.drawable.kriminal
            ),
            KinoItem(8, "Побег из Шоушенка", "Страх - это кандалы. Надежда - это свобода.",
                R.drawable.pobeg
            ),
            KinoItem(9, "Форрест Гамп", "Жизнь как коробка шоколадных конфет.", R.drawable.forrest),
            KinoItem(10, "Бойцовский клуб", "Первое правило Бойцовского клуба...",
                R.drawable.boicovski
            )
        )
    }
}