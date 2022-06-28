package nordis.nordisquiz

class QuestCreator {

    fun createQuestions() {
        if (questResponseList.isEmpty()) {
            questResponseList.add(
                QuestionClass(
                    "С какой из этих стран Чехия не граничит?",
                    "Венгрия", "Германия", "Австрия", "Польша"
                )
            )
            questResponseList.add(
                QuestionClass(
                    "Кто считается основоположником кубизма?",
                    "П. Пикассо", "В. Кандинский", "Ф. Леже", "К. Малевич"
                )
            )
            questResponseList.add(
                QuestionClass(
                    "Кто из этих знаменитых людей не является тезкой остальных?",
                    "Лужков", "Боярский", "Лермонтов", "Горбачев"
                )
            )

            questResponseList.add(
                QuestionClass(
                    "Какая березка стояла во поле?",
                    "Кудрявая", "Засохшая", "Зеленая", "Высокая"
                )
            )

            questResponseList.add(
                QuestionClass(
                    "Какая из этих кислот является витамином?",
                    "Никотиновая", "Янтарная", "Яблочная", "Молочная"
                )
            )

            questResponseList.add(
                QuestionClass(
                    "Территория какой из этих стран - наибольшая?",
                    "Япония", "Финляндия", "Италия", "Германия"
                )
            )

            questResponseList.add(
                QuestionClass(
                    "Какой из этих городов - родина Казановы?",
                    "Венеция", "Флоренция", "Милан", "Неаполь"
                )
            )

            questResponseList.add(
                QuestionClass(
                    "Чье произведение легло в основу оперы Дж. Верди 'Травиата'?",
                    "А.Дюма-сына", "Г. Флобера", "О. Бальзака", "В. Гюго"
                )
            )

            questResponseList.add(
                QuestionClass(
                    "Кто является чемпионом гонок 'Формулы-1' 1998-99 г.г.?",
                    "Хаккинен", "Кулхард", "Барикелло", "М. Шумахер"
                )
            )
        }
    }
}