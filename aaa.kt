data class Response(
    val code: Int,
    val body: String?,
)

class Client {
    fun perform(code: Int, body: String?) = ResponseActions(code, body)
}

class ResponseActions(private val code: Int, private val body: String?){

    inner class AndExpect{
        fun status(statusResponseMatchers: StatusResponseMatchers.() -> ResponseActions) {
            StatusResponseMatchers().statusResponseMatchers()
        }
        fun body(statusResponseMatchers: BodyResponseMatchers.() -> ResponseActions){
            BodyResponseMatchers().statusResponseMatchers()
        }
    }

    inner class BodyResponseMatchers{
        fun isNull(): ResponseActions{
            if (body == null){throw BodyResponseMatchersException()}
            return ResponseActions(code, body)
        }
        fun isNotNull(): ResponseActions{
            return ResponseActions(code, body)
        }
    }

    inner class StatusResponseMatchers{
        fun isOk(): ResponseActions {
            if (code != 200){throw StatusResponseMatchersException()}
            return ResponseActions(code, body)
        }
        fun isBadRequest(): ResponseActions {
            if (code != 400){throw StatusResponseMatchersException()}
            return ResponseActions(code, body)
        }
        fun isInternalServerError(): ResponseActions {
            if (code != 500){throw StatusResponseMatchersException()}
            return ResponseActions(code, body)
        }
    }

    fun andExpect(andExpect: AndExpect.() -> Unit): ResponseActions{
        andExpect(this.AndExpect())
        return ResponseActions(code, body)
    }

    fun andDo(response: (Response) -> Unit): ResponseActions{
        response(Response(code, body))
        return ResponseActions(code, body)
    }
}

sealed class ResponseMatchersException: Exception()

class StatusResponseMatchersException: ResponseMatchersException()

class BodyResponseMatchersException: ResponseMatchersException()

fun main(){
    val mockClient = Client()
    val response = mockClient.perform(200, "OK")
        .andExpect {
            status { isOk() }
            body { isNotNull() }
        }.andDo { response -> println(response) }
}