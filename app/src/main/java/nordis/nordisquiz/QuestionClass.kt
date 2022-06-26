package nordis.nordisquiz

class QuestionClass {
    var question: String? = null
    var rightResponse: String? = null
    var response2: String? = null
    var response3: String? = null
    var response4: String? = null

    constructor(
        question: String?,
        rightResponse: String?,
        response2: String?,
        response3: String?,
        response4: String?
    ) {
        this.question = question
        this.rightResponse = rightResponse
        this.response2 = response2
        this.response3 = response3
        this.response4 = response4
    }
}