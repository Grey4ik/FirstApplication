package com.example.firstapplication

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaterTrackerUiTest {

    // Правило для запуска Compose-теста (как в практике)
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * Задание 9.1: Проверить наличие и корректную работу кнопки добавления жидкости
     */
    @Test
    fun testAddWaterButtonWorks() {
        // Запускаем экран
        composeTestRule.setContent { WaterTracker() }

        // Проверяем начальное состояние
        composeTestRule.onNodeWithTag("water_count_text").assertTextEquals("0 мл")

        // Нажимаем кнопку добавления
        composeTestRule.onNodeWithTag("add_water_button").performClick()

        // Проверяем, что счетчик увеличился на 250
        composeTestRule.onNodeWithTag("water_count_text").assertTextEquals("250 мл")
    }

    /**
     * Задание 9.2: Проверить наличие и корректную работу кнопки "Завершить день"
     * Сценарий: День завершен с недостаточным количеством воды (< 1500 мл)
     */
    @Test
    fun testCompleteDayButtonResetsCounter() {
        composeTestRule.setContent { WaterTracker() }

        // Добавляем 1000 мл (4 клика), что меньше нормы
        repeat(4) {
            composeTestRule.onNodeWithTag("add_water_button").performClick()
        }
        composeTestRule.onNodeWithTag("water_count_text").assertTextEquals("1000 мл")

        // Нажимаем "Завершить день"
        composeTestRule.onNodeWithTag("complete_day_button").performClick()

        // Счетчик воды должен сброситься в 0
        composeTestRule.onNodeWithTag("water_count_text").assertTextEquals("0 мл")

        // Серия дней должна остаться 0 (так как норма не выполнена)
        composeTestRule.onNodeWithTag("streak_days_text").assertTextContains("0")
    }

    /**
     * Задание 9.3: Проверить вывод текста количества дней подряд (>= 1500 мл)
     * Сценарий: День завершен с выполненной нормой
     */
    @Test
    fun testStreakIncrementsWhenGoalMet() {
        composeTestRule.setContent { WaterTracker() }

        // Проверяем начальный текст серии
        composeTestRule.onNodeWithTag("streak_days_text").assertTextContains("0")

        // Добавляем ровно 1500 мл (6 кликов по 250)
        repeat(6) {
            composeTestRule.onNodeWithTag("add_water_button").performClick()
        }
        composeTestRule.onNodeWithTag("water_count_text").assertTextEquals("1500 мл")

        // Завершаем день
        composeTestRule.onNodeWithTag("complete_day_button").performClick()

        // Счетчик воды сброшен
        composeTestRule.onNodeWithTag("water_count_text").assertTextEquals("0 мл")

        // Серия дней должна увеличиться до 1
        composeTestRule.onNodeWithTag("streak_days_text").assertTextContains("1")
    }
}

// Вспомогательные функции для удобных проверок (Best Practice)
private fun SemanticsNodeInteraction.assertTextEquals(expected: String) {
    this.assert(hasText(expected))
}

private fun SemanticsNodeInteraction.assertTextContains(expected: String) {
    this.assert(hasText(expected, substring = true))
}
