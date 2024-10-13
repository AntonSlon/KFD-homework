const val PI = 3.14

interface ConsoleService {
    fun getOperation(number: Int): Any
    fun work()
}

interface FigureService {
    fun addSquare(property: Double)
    fun addCircle(property: Double)
    fun getPerimeter()
    fun getArea()
    fun addFigure(property: Double)
}

sealed interface Figure {
    val property : Double
    fun computeArea(): Double
    fun computePerimeter(): Double
}

class Square(override val property : Double): Figure{
    override fun computeArea(): Double {
        return property * property
    }

    override fun computePerimeter(): Double {
        return 4 * property
    }


class Circle(override val property : Double): Figure{
    override fun computeArea(): Double {
        return 2 * PI * property
    }

    override fun computePerimeter(): Double {
        return PI * property * property
    }
}

private object FigureServiceImp{
    private val figureList: MutableList<Figure> = mutableListOf()

    fun getFigureList(): MutableList<Figure>{
        return figureList
    }
}

private object ConsoleServiceImpl: FigureService {
    override fun addSquare(property: Double) {
        FigureServiceImp.getFigureList().add(Square(property))
    }

    override fun addCircle(property: Double) {
        FigureServiceImp.getFigureList().add(Circle(property))
    }

    override fun getPerimeter() {
        FigureServiceImp.getFigureList().forEach { println(it.computePerimeter()) }
    }

    override fun getArea() {
        FigureServiceImp.getFigureList().forEach { println(it.computeArea()) }
    }

    override fun addFigure(property: Double) {
        println("""|Введите тип фигуры:
            |1 - квадрат
            |2 - круг""".trimMargin())
        when (readln().toInt()) {
            1 -> addSquare(property)
            2 -> addCircle(4.0)
        }
    }
}

enum class Operation{
    INSERT,
    GET_AREA,
    GET_PERIMETER,
    EXIT
}

object Console: ConsoleService{
    override fun getOperation(number: Int): Any{
        return when (number) {
            1 ->  Operation.INSERT
            2 ->  Operation.GET_PERIMETER
            3 ->  Operation.GET_AREA
            4 ->  Operation.EXIT
            else -> throw WrongOperationTypeException()
        }
    }
    override fun work() {
        while (true) {
            try {
                println(
                    """Введите тип операции:
                | 1 - добавить фигуру
                | 2 - получить периметр всех фигур
                | 3 - получить площадь всех фигур
                | 4 - завершить работу""".trimMargin())
                val typeOfOperation = readln().toInt()
                val operation = getOperation(typeOfOperation)
                when (operation) {
                    Operation.INSERT -> {
                        println("Введите property")
                        val property = readln().toDouble()
                        if (property < 1) throw BadPropertyException()
                        ConsoleServiceImpl.addFigure(property)
                    }
                    Operation.GET_PERIMETER -> ConsoleServiceImpl.getPerimeter()
                    Operation.GET_AREA -> ConsoleServiceImpl.getArea()
                    Operation.EXIT -> break
            }
            }catch (e: BadPropertyException){
            println("property < 0")
        }catch (e: WrongOperationTypeException){
            println("Неизвестный тип операции")
        }catch (e: WrongFigureTypeException){
            println("Неизвестынй тип фигуры")
        }
        }
    }
}

class BadPropertyException: Exception()

class WrongOperationTypeException: Exception()

class WrongFigureTypeException: Exception()

fun main() {
    Console.work()
}