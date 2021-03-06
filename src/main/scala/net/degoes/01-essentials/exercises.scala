// Copyright(C) 2018 - John A. De Goes. All rights reserved.

package net.degoes.essentials

import java.io.File
import java.util.Date

import scala.util.Try



object types {
  // Values are everything on left side of the equals sign
  // not everything is a value, types are not values, packages are not values
  // methods are not values, statements are not values
  // types are sets of values
  // Nothing is the set with no members
  type ??? = Nothing

  //ref is a element of the set Bool
  //this is a proposition, compiler helps you prove it
  // Boolean = { true, false}
  //Every type error is a logical contradiction found by your compiler
  // Int = { x | x is integer that can fit in 4 bytes} -> set builder notation
  // Long = { X | X is in 8 bytes }
  val ref : Boolean = ???
  //
  // EXERCISE 1
  //
  // List all values of the type `Unit`.
  //
  val UnitValues: Set[Unit] = Set(())

  //
  // EXERCISE 2
  //
  // List all values of the type `Nothing`.
  //
  val NothingValues: Set[Nothing] = Set.empty

  //
  // EXERCISE 3
  //
  // List all values of the type `Boolean`.
  //
  val BoolValues: Set[Boolean] = Set(true, false)

  //
  // EXERCISE 4
  //
  // List all values of the type `Either[Unit, Boolean]`.
  //
  val EitherUnitBoolValues: Set[Either[Unit, Boolean]] = Set(Left(()),Right(true), Right(false))

  //
  // EXERCISE 5
  //
  // List all values of the type `(Boolean, Boolean)`.
  //
  val TupleBoolBoolValues: Set[(Boolean, Boolean)] = Set((true,true),(true,false),(false,true),(false,false))

  //
  // EXERCISE 6
  //
  // List all values of the type `Either[Either[Unit, Unit], Unit]`.
  //
  val EitherEitherUnitUnitUnitValues: Set[Either[Either[Unit, Unit], Unit]] = Set(
    Right(Right(())),
    Right(Left(())),
    Left(Right(())),
    Left(Left(()))
  )

  //Make illegal states unrepresentable
  // |A| = n
  // A = { a2, a2 ...,  an}
  // |B| = m
  // B = { b1, b2 ..., bm }
  // A * B = { (a, b) | A a: A, b: B }\
  //        = { (a1, b1), (a1, b2) ... ( a1, bm),
  //|A * B| = n * m
  //product types
  val point : (Int, Int) = (7,5)
  val point3 : (Int,(Int,Int)) = (1,(2,3))
  case class Point(x : Int, y : Int)

  //Geometric Interpretation of Product Types
  // each product type value is coordinate in n dimensional space
  //
  // EXERCISE 7
  //
  // Given:
  // A = { true, false }
  // B = { "red", "green", "blue" }
  //
  // List all the elements in `A * B`.
  //
  val AProductB: Set[(Boolean, String)] = Set(
    (true,"red"),(true,"green"),(true,"blue"),
    (false,"red"),(false,"green"),(false,"blue"))

  // A = { a1 .. an }
  // B = { a1 .. bm }
  //A + B = {x | x : A OR x : B }
  //== {a1 .. an, b1 .. bm }
  // Either is dual to Tuple
  sealed trait CoffeePreference // without sealed size of set not constrained
  case object Black extends CoffeePreference
  case object WithCream extends CoffeePreference
  case class Both(l : CoffeePreference, r : CoffeePreference) extends CoffeePreference

  //make all your case classes final and all your traits sealed
  // abstract class vs sealed trait - abstract class better for binary compatibilty
  sealed trait Either[+A, +B]
  final case class Left[A](value : A) extends Either[A,Nothing]
  final case class Right[A](value : A) extends Either[Nothing, A]
  //Sql has no Sum type


  //
  // EXERCISE 8
  //
  // Given:
  // A = { true, false }
  // B = { "red", "green", "blue" }
  //
  // List all the elements in `A + B`.
  //
  val ASumB: Set[Either[Boolean, String]] = Set(
    Left(true),
    Left(false),
    Right("red"),
    Right("green"),
    Right("blue")
  )

  //geometric interpretation
  // each A + B in sum type, represent by laying end to end

  //
  // EXERCISE 9
  //
  // Create a product type of `Int` and `String`, representing the age and
  // name of a person.
  //
  type Person1 = ???
  case class Person2(/*  */)

  //
  // EXERCISE 10
  //
  // Prove that `A * 1` is equivalent to `A` by implementing the following two
  // functions.
  //
  //equivalent means bijection means isomorphism
  // we prove they are equvilant by writing a total function from one to the other, and then back
  def to1[A](t: (A, Unit)): A = t._1
  def from1[A](a: A): (A, Unit) = (a,())

  //
  // EXERCISE 11
  //
  // Prove that `A * 0` is equivalent to `0` by implementing the following two
  // functions.
  //
  //if you give me a nothing, I can create any type
  def to2[A](t: (A, Nothing)): Nothing = t._2
  def from2[A](n: Nothing): (A, Nothing) = (n,n)

  //Alternative view of Nothing
  final abstract class Void {
    def absurd[A] : A // we can never implement this
  }

  def to2[A](t: (A, Void)): Void = t._2
  def from2[A](n: Void): (A, Void) =  n.absurd[(A, Void)]
  //
  // EXERCISE 12
  //
  // Create a sum type of `Int` and `String` representing the identifier of
  // a robot (a number) or the identifier of a person (a name).
  //
  type Identifier1 = Either[Int, String]
  sealed trait Identifier2
  case class Robot(id : Int) extends Identifier2
  case class Person(id : String) extends Identifier2

  //
  // EXERCISE 13
  //
  // Prove that `A + 0` is equivalent to `A` by implementing the following two
  // functions.
  //
  def to3[A](t: Either[A, Nothing]): A = t match {
    case Left(a) => a
    case Right(n) => n
  }
  def from3[A](a: A): Either[A, Nothing] = Left(a)

  //
  // EXERCISE 14
  //
  // Create either a sum type or a product type (as appropriate) to represent a
  // credit card, which has a number, an expiration date, and a security code.
  //
  type CreditCard = (Long,String,String)

  //
  // EXERCISE 15
  //
  // Create either a sum type or a product type (as appropriate) to represent a
  // payment method, which could be a credit card, bank account, or
  // cryptocurrency.
  //
  sealed trait Payment
  case class CC() extends Payment
  case class BankAccount() extends Payment
  case class BitCoin() extends Payment
  type PaymentMethod = Payment

  //
  // EXERCISE 16
  //
  // Create either a sum type or a product type (as appropriate) to represent an
  // employee at a company, which has a title, salary, name, and employment date.
  //
  case class Employee1(title : String, salary : Long, name : String, date : Date)
  type Employee = ???

  //
  // EXERCISE 17
  //
  // Create either a sum type or a product type (as appropriate) to represent a
  // piece on a chess board, which could be a pawn, rook, bishop, knight,
  // queen, or king.
  //
//  sealed trait ChessPiece
//  final case class Pawn extends ChessPiece
//  final case class
//  type ChessPiece = ???

  //
  // EXERCISE 18
  //
  // Create an ADT model of a game world, including a map, a player, non-player
  // characters, different classes of items, and character stats.
  //
  type GameWorld = ???
//  how easy of it to create a value of game world that is not an illegal state

  case class GameWorld1(
    map : GameMap,
    player : Character
  )
  case class GameMap(location: Set[Location],
    routes : Location => Set[Location])

  case class Name private (value : String)
object Name {
  def apply(name : String): Option[Name] =
    if (name.trim.length == 0) None
    else Some(new Name(name))
}

  type Stats = ???
  case class Character(name : Name, description : String, inventory : Set[Item], stats : Stats)
  sealed trait Location
  case object Forest extends Location

  sealed trait Item
  case class MagicalItem(name : String) extends Item

  case class States(health : Int)

}

object functions {
  type ??? = Nothing

  //this type is lie, because not total
  def foo(x: Int): String = x match {
    case 1 => "foo"
  }

  //this is a lie, null is not a string
  // non total functions are not a lit
  def foo1(x: Int): String = null

  //last company - 100k+ scala and never went down -jdegoes

  //All functions `f: A => B` satisfy the following properties:
  // 1) Totality if `a:A` then `f(a) : B `
  // 2) Determinism. If `a : A`, `b : A`, and `a = b`, then `f(a) == f(b)`.
  // 3) Purity. The only effect of evaluation `f(a)` is computing the return value

  //Every method on an object, has an parameter this, that is implicit

  //function is mapping between two sets
  // source is domain, result is codomain
  //
  // EXERCISE 1
  //
  // Convert the following non-function into a function.
  //
  def parseInt1(s: String): Int = s.toInt

  def parseInt2(s: String): Option[Int] = Try(s.toInt).toOption

  //
  // EXERCISE 2
  //
  // Convert the following non-function into a function.
  //
  def arrayUpdate1[A](arr: Array[A], i: Int, f: A => A): Unit =
    arr.update(i, f(arr(i)))
//
  def arrayUpdate2[A](arr: Array[A], i: Int, f: A => A): Option[Array[A]] =
    if (i < 0 || i >= arr.length) {
      None
    } else {
      arr.clone()
      arr.update(i, f(arr(i)))
      Some(arr)
    }

  //
  // EXERCISE 3
  //
  // Convert the following non-function into a function.
  //
  def divide1(a: Int, b: Int): Int = a / b

  def divide2(a: Int, b: Int): Option[Int] = if (b == 0) {
    None
  } else {
    Some(a / b)
  }

  //
  // EXERCISE 4
  //
  // Convert the following non-function into a function.
  //
  var id = 0

  def freshId1(): Int = {
    val newId = id
    id += 1
    newId
  }

  def freshId2(id: Int): (Int, Int) = (id, id + 1)

  //
  // EXERCISE 5
  //
  // Convert the following non-function into a function.
  //
  import java.time.LocalDateTime

  def afterOneHour1: LocalDateTime = LocalDateTime.now.plusHours(1)

  def afterOneHour2(currentTime: LocalDateTime): LocalDateTime = currentTime.plusHours(1)

  //
  // EXERCISE 6
  //
  // Convert the following non-function into function.
  //
  def head1[A](as: List[A]): A = {
    if (as.length == 0) println("Oh no, it's impossible!!!")
    as.head
  }

  def head2[A](as: List[A]): Option[A] = as.headOption

  //
  // EXERCISE 7
  //
  // Convert the following non-function into a function.
  //
  trait Account

  trait Processor {
    def charge(account: Account, amount: Double): Unit
  }

  case class Coffee() {
    val price = 3.14
  }

  def buyCoffee1(processor: Processor, account: Account): Coffee = {
    val coffee = Coffee()
    processor.charge(account, coffee.price)
    coffee
  }

  final case class Charge(account: Account, amount: Double)

  def buyCoffee2(account: Account): (Coffee, Charge) = ???

  //
  // EXERCISE 8
  //
  // Implement the following function under the Scalazzi subset of Scala.
  //
  def printLine(line: String): Unit = ()

  //
  // EXERCISE 9
  //
  // Implement the following function under the Scalazzi subset of Scala.
  //
  def readLine: String = "Adam"

  //
  // EXERCISE 10
  //
  // Implement the following function under the Scalazzi subset of Scala.
  //
  def systemExit(code: Int): Unit = ()

  //
  // EXERCISE 11
  //
  // Rewrite the following non-function `printer1` into a pure function, which
  // could be used by pure or impure code.
  //
  def printer1(): Unit = {
    println("Welcome to the help page!")
    println("To list commands, type `commands`.")
    println("For help on a command, type `help <command>`")
    println("To exit the help page, type `exit`.")
  }

  def printer2[A](println: String => A, combine: (A, A) => A): A = {
    printLine("one")
    ???
  }

  printer2(println, (x: Unit, y: Unit) => ())

  //
  // EXERCISE 12
  //
  // Create a purely-functional drawing library that is equivalent in
  // expressive power to the following procedural library.
  //
  trait Draw {
    def goLeft(): Unit
    def goRight(): Unit
    def goUp(): Unit
    def goDown(): Unit
    def draw(): Unit
    def finish(): List[List[Boolean]]
  }

  def draw1(size: Int): Draw = new Draw {
    val canvas = Array.fill(size, size)(false)
    var x = 0
    var y = 0

    def goLeft(): Unit = x -= 1

    def goRight(): Unit = x += 1

    def goUp(): Unit = y += 1

    def goDown(): Unit = y -= 1

    def draw(): Unit = {
      def wrap(x: Int): Int =
        if (x < 0) (size - 1) + ((x + 1) % size) else x % size

      val x2 = wrap(x)
      val y2 = wrap(y)

      canvas.updated(x2, canvas(x2).updated(y2, true))
    }

    def finish(): List[List[Boolean]] =
      canvas.map(_.toList).toList
  }

  //intial encoding

//  type Point = (Int, Int)
//  case class Canvas1(current : Point, drawn : Array[Array[Boolean]])
//  object Canvas1{
//    def create(size: Int) : Canvas1 = {
//      val fill = Array.fill(size, size)(false)
//      Canvas1((0,0), fill)
//    }
//  }
//
//  trait Draw1 {
//    def goLeft(c : Point): Point
//    def goRight(c : Point): Point
//    def goUp(c : Point): Point
//    def goDown(c : Point): Point
//    def draw(c : Point): Point
//    def finish(): List[List[Boolean]]
//  }
//
//
//  def draw2(size: Int): Draw1 = new Draw1 {
//    def goLeft(c : Point): Point = (c._1 - 1, c._2)
//    def goRight(c : Point): Point = (c._1 + 1, c._2)
//    def goUp(c : Point): Point = (c._1 , c._2 + 1)
//    def goDown(c : Point): Point = (c._1, c._2 - 1)
//
//    def draw(canvas1: Canvas1): Canvas1 = {
//      Canvas1(canvas1.current,mark(canvas1.current,canvas1.drawn))
////      val copied: Array[Array[Boolean]] = canvas1.drawn.clone()
////      val mod = copied.update(canvas1.current._1, copied(canvas1.current._1).clone().update(canvas1.current._2, true))
////        Canvas1(canvas1.current, mod)
//    }
//
//    def mark(p : Point, arr : Array[Array[Boolean]]) : Array[Array[Boolean]] = ???
//
//    def finish(canvas1 : Canvas1): List[List[Boolean]] =
//      canvas1.drawn.map(_.toList).toList
//  }
//
//  //Johns version
//  case class Point(x : Int, y : Int)
//  case class BitMap(value : List[List[Boolean]])
//
//  type DrawFunction = (Point, BitMap) => (Point, BitMap)
//
//  val goRight : DrawFunction = _ match {
//    case (Point(x, y), bitmap) => (Point(x + 1, y), bitmap)
//  }
//
//  goRight.andThen(goRight)
//
//  //Or
//  sealed trait DrawCommand
//  case object GoLeft extends DrawCommand
//  case object GoRight extends DrawCommand
//
//  def draw(size : Int, commands : List[DrawCommand]) : List[List[Boolean]] = {
//    def optimize(c : List[DrawCommand]) : List[DrawCommand] =
//      c match {
//        case GoLeft :: GoRight :: rest => optimize(rest)
//        case x :: rest => x :: optimize(rest)
//      }
//    ???
//  }
//
//  def compile(c : List[DrawCommand] ): Array[Array[Boolean]] => Unit = ???
  //see gitter

//
//  sealed trait DrawCommand
//  case object GoLeft extends DrawCommand
//  case object GoRight extends DrawCommand
//  case object GoUp extends DrawCommand
//  case object GoDown extends DrawCommand
//  case object Draw extends DrawCommand
//
//  def draw(size: Int, commands: List[DrawCommand]): List[List[Boolean]] = {
//    def optimize(c: List[DrawCommand]): List[DrawCommand] =
//      c match {
//        case GoLeft :: GoRight :: rest => optimize(rest)
//        case GoRight :: GoLeft :: rest => optimize(rest)
//        case GoUp :: GoDown :: rest => optimize(rest)
//        case GoDown :: GoUp :: rest => optimize(rest)
//        case x :: rest => x :: optimize(rest)
//      }
//    val optimized = optimize(commands)
//
//    var x : Int = 0
//    var y : Int = 0
//
//    def compile(c: List[DrawCommand]): Array[Array[Boolean]] => Unit =
//    ...
//
//    compile(optimized)(...).toList.map(_.toList)
//  }

  //function oriented solution
  // final encoding
//case class Point(x: Int, y: Int)
//  case class Bitmap(value: List[List[Boolean]])
//
//  type DrawFunction = (Point, Bitmap) => (Point, Bitmap)
//
//  val goLeft: DrawFunction = _ match {
//    case (Point(x, y), bitmap) => (Point(x - 1, y), bitmap)
//  }
//  val goRight: DrawFunction = _ match {
//    case (Point(x, y), bitmap) => (Point(x + 1, y), bitmap)
//  }
//  val goDown: DrawFunction = _ match {
//    case (Point(x, y), bitmap) => (Point(x, y - 1), bitmap)
//  }
//  val goUp: DrawFunction = _ match {
//    case (Point(x, y), bitmap) => (Point(x, y + 1), bitmap)
//  }
//  val draw: DrawFunction = _ match {
//    case (Point(x, y), bitmap) =>
//      def wrap(x: Int): Int =
//        if (x < 0) (size - 1) + ((x + 1) % size) else x % size
//
//      val x2 = wrap(x)
//      val y2 = wrap(y)
//
//      (Point(x, y), bitmap.updated(x2, bitmap(x2).updated(y2, true)))
//  }
//  goDown.andThen(goRight)
//    .andThen(draw)
//    .andThen(goRight)
//    .andThen(draw)
//    .andThen(goRight)
//    .andThen(draw)
}

object higher_order {
  //
  // EXERCISE 1
  //
  // Implement the following higher-order function.
  //
  def fanout[A, B, C](f: A => B, g: A => C): A => (B, C) = a => (f(a),g(a))

  //
  // EXERCISE 2
  //
  // Implement the following higher-order function.
  //
  def cross[A, B, C, D](f: A => B, g: C => D): (A, C) => (B, D) =
    (x,y) => (f(x),g(y))


  def sort[A](list : List[A]) : List[A] = ???
  //Can be thought of (A) => List[A] => List[A]
  // Where the (A) part of is applied at compile time
  //
  // EXERCISE 3
  //
  // Implement the following higher-order function.
  //
  def either[A, B, C](f: A => B, g: C => B): Either[A, C] => B =
    _.fold(f,g)

  //
  // EXERCISE 4
  //
  // Implement the following higher-order function.
  //
  def choice[A, B, C, D](f: A => B, g: C => D): Either[A, C] => Either[B, D] =
    param => param match {
      case Right(r) => Right(g(r))
      case Left(l) => Left(f(l))
    }

  //
  // EXERCISE 5
  //
  // Implement the following higer-order function.
  //
  def compose[A, B, C](f: B => C, g: A => B): A => C =
    x => f(g(x))

  //
  // EXERCISE 6
  //
  // Implement the following higher-order function. After you implement
  // the function, interpret its meaning.
  //
  //Always shadow - jdegoes
  // the fp card game, where function are the cards,
  //start with then end state, with type holes and try to get there

  def alt[E1, E2, A, B](l: Parser[E1, A], r: E1 => Parser[E2, B]):
    Parser[E2, Either[A, B]] = Parser { (x : String) =>
    val x1: Either[E1, (String, A)] = l.run(x)
    x1 match {
      case Left(e1) =>
        r(e1).run(x) match {
          case Left(e2) => Left(e2)
          case Right((input, b)) => Right((input, Right(b)))
      }
      case Right((input, a))  =>
        Right((input, Left(a)))
    }
  }

  case class Parser[+E, +A](run: String => Either[E, (String, A)])
  object Parser {
    final def fail[E](e: E): Parser[E, Nothing] =
      Parser(_ => Left(e))

    final def point[A](a: => A): Parser[Nothing, A] =
      Parser(input => Right((input, a)))

    final def char[E](e: E): Parser[E, Char] =
      Parser(input =>
        if (input.length == 0) Left(e)
        else Right((input.drop(1), input.charAt(0))))
  }
}

object poly_functions {


  def identity[A](a : A): A = a
  //
  // EXERCISE 1
  //
  // Create a polymorphic function of two type parameters `A` and `B` called
  // `snd` that returns the second element out of any pair of `A` and `B`.
  //
  object snd {
    def apply[A, B](t: (A, B)): B = t._2
  }
  snd((1, "foo")) // "foo"
  snd((true, List(1, 2, 3))) // List(1, 2, 3)

  //
  // EXERCISE 2
  //
  // Create a polymorphic function called `repeat` that can take any
  // function `A => A`, and apply it repeatedly to a starting value
  // `A` the specified number of times.
  //
  object repeat {
    def apply[A](n: Int)(a: A, f: A => A): A =
      if(n == 0) f(a)
      else apply(n -1)(f(a),f)
  }
  repeat[   Int](100)( 0, _ +   1) // 100
  repeat[String]( 10)("", _ + "*") // "**********"

  //
  // EXERCISE 3
  //
  // Count the number of unique implementations of the following method.
  //
  def countExample1[A, B](a: A, b: B): Either[A, B] = ???
  val countExample1Answer = ???

  //
  // EXERCISE 4
  //
  // Count the number of unique implementations of the following method.
  //
  def countExample2[A, B](f: A => B, g: A => B, a: A): B =
    ???
  val countExample2Answer = ???

  //
  // EXERCISE 5
  //
  // Implement the function `groupBy1`.
  //
  val Data =
    "poweroutage;2018-09-20;level=20" :: Nil
  val By: String => String =
    (data: String) => data.split(";")(1)
  val Reducer: (String, List[String]) => String =
    (date, events) =>
      "On date " +
        date + ", there were " +
        events.length + " power outages"
  val Expected =
    Map("2018-09-20" ->
      "On date 2018-09-20, there were 1 power outages")
  def groupBy1(
    l: List[String],
    by: String => String)(
      reducer: (String, List[String]) => String):
      Map[String, String] = {
//     val dates: Map[String, List[String]] = l.groupBy(by)
//    dates.map(x => reducer)
    ???
  }
  // groupBy1(Data, By)(Reducer) == Expected

  //
  // EXERCISE 6
  //
  // Make the function `groupBy1` as polymorphic as possible and implement
  // the polymorphic function. Compare to the original.
  //
  //Orig, Proj, Summary
  //Object apply makes it easier to apply
  object groupBy2 {
    def apply[A,B,C](
      l: List[A],
      by: A => B)(
      reducer: (B, List[A]) => C):
    Map[B, C] = {
      l.groupBy(by).map(t => (t._1, reducer(t._1,t._2)))
    }
}

object higher_kinded {

//  type List[A] = ???
  val foo = ??? // <- values go here
  val foo1 : ?? = ??? // type goes in ?? place
  //  val foo2 : List = ??? //invalid
  // Set of List = { nothing makes sense to go here }
  case class Person(age : Int, name : String) // data constructor -> feed it data it give you bakc a person

  //List is a type constructor, it needs to b e fed a type to be a type
  // A type constructor is a tye level function
  // object List { def apply( t : Type) : List[T] }
  // * = { x | x is a type }
  // = { Int, List[Int], String, Boolean ... }
  // List : * => * // list is a function from Type to Type
  // Int : *  //
  // List : * => *
  // * => * { x | x has kind `* => * }
  //        = { List, Option, }
  // [*, *] => * = {Map, Either, Tuple2}
  // [ *, *, * ] => { tuple3 }

  //All type params have kind *
  //X[_] has a kind of * -> *
  //X[A] is equiv to above, but A is defined
  //X[A, B <: A] for instance
  trait Algorithm[Container[_]] {
    def run[A](container : Container[A]) : Int = ???
  }

  //higher order kind is the kind of a thing that takes a type constructor
  val listAlgo : Algorithm[List] = ??? //higher order kind
  // Algorithm : (* => *) => *
  def update(f : Int => Int): Int = ??? //higher order function

  // A[_] : * => *
  // B[_[_]] : * => * => *
  // C[_, _[_], _] : [*, * => *, *] => *
}
  type ?? = Nothing
  type ???[A] = Nothing
  type ????[A, B] = Nothing
  type ?????[F[_]] = Nothing

  trait `* => *`[F[_]]
  trait `[*, *] => *`[F[_, _]]
  trait `(* => *) => *`[T[_[_]]]

  //
  // EXERCISE 1
  //
  // Identify a type constructor that takes one type parameter of kind `*`
  // (i.e. has kind `* => *`), and place your answer inside the square brackets.
  //
  type Answer1 = `* => *`[List]

  //
  // EXERCISE 2
  //
  // Identify a type constructor that takes two type parameters of kind `*` (i.e.
  // has kind `[*, *] => *`), and place your answer inside the square brackets.
  //
  type Answer2 = `[*, *] => *`[Map]

  //
  // EXERCISE 3
  //
  // Create a new type called `Answer3` that has kind `*`.
  //
  trait Answer3 /*[]*/

  //
  // EXERCISE 4
  //
  // Create a trait with kind `[*, *, *] => *`.
  //
  trait Answer4[A,B,C] /*[]*/

  //
  // EXERCISE 5
  //
  // Create a new type that has kind `(* => *) => *`.
  //
  type NewType1[A[_]]  /* ??? */
  type Answer5 = `(* => *) => *`[NewType1]

  //
  // EXERCISE 6
  //
  // Create a trait with kind `[* => *, (* => *) => *] => *`.
  //
  trait Answer6[A[_], B[_[_]]] /*[]*/


  val plus : (Int, Int) => Int = (a,b) => a + b
  val list : List[Int] = ???

  list.map(plus(1, _))

  //
  // EXERCISE 7
  //
  // Create an implementation of the trait `CollectionLike` for `List`.
  //
  trait CollectionLike[F[_]] {
    def empty[A]: F[A]

    def cons[A](a: A, as: F[A]): F[A]

    def uncons[A](as: F[A]): Option[(A, F[A])]

    final def singleton[A](a: A): F[A] =
      cons(a, empty[A])

    final def append[A](l: F[A], r: F[A]): F[A] =
      uncons(l) match {
        case Some((l, ls)) => append(ls, cons(l, r))
        case None => r
      }

    final def filter[A](fa: F[A])(f: A => Boolean): F[A] =
      bind(fa)(a => if (f(a)) singleton(a) else empty[A])

    final def bind[A, B](fa: F[A])(f: A => F[B]): F[B] =
      uncons(fa) match {
        case Some((a, as)) => append(f(a), bind(as)(f))
        case None => empty[B]
      }

    final def fmap[A, B](fa: F[A])(f: A => B): F[B] = {
      val single: B => F[B] = singleton[B](_)

      bind(fa)(f andThen single)
    }
  }
  val ListCollectionLike: CollectionLike[List] = new CollectionLike[List] {
    override def empty[A]: List[A] = List.empty

    override def cons[A](a: A, as: List[A]): List[A] = a :: as

    override def uncons[A](as: List[A]): Option[(A, List[A])] = as match {
      case a :: as => Option((a, as))
      case _ => None
    }
  }

  //
  // EXERCISE 8
  //
  // Implement `Sized` for `List`.
  //
  trait Sized[F[_]] { // * => * => *
    // This method will return the number of `A`s inside `fa`.
    def size[A](fa: F[A]): Int
  }
  val ListSized: Sized[List] = new Sized[List] {
    override def size[A](fa: List[A]): Int = fa.length
  }

  // ? is the underscore at the type level
  //
  // EXERCISE 9
  //
  // Implement `Sized` for `Map`, partially applied with its first type
  // parameter to `String`.
  //

  val MapStringSized: Sized[Map[String, ?]] = new Sized[Map[String,?]] {
    override def size[A](fa: Map[String, A]): Int = fa.seq.toList.length
  }

  //
  // EXERCISE 10
  //
  // Implement `Sized` for `Map`, partially applied with its first type
  // parameter to a user-defined type parameter.
  //
  def MapSized2[K]: Sized[Map[K, ?]] = new Sized[Map[K,?]] {
    override def size[A](fa: Map[K, A]): Int = fa.seq.toList.length
  }

  //
  // EXERCISE 11
  //
  // Implement `Sized` for `Tuple3`.
  //
//  def Tuple3Sized[C, B]: Sized[Tuple3[?,?,?]] = new Sized[Tuple3[A, C,B]] {}
}

object tc_motivating {

  def sort(list : List[Int]) : List[Int] = ???
  def sort1[A](list : List[A]) : List[A] = ??? // not enough information

  //not transitive
//  val lessThan1 : (Int, Int) => Boolean = {
//    case (1 ,2) => true
//    case (2, 3) => true
//    case (1, 3) => false
//  }

  def sort1[A](list : List[A]) (implicit lessThan: (A,A) => Boolean): List[A] = ??? // not enough information

  trait LessThan1[A]{
    def lessThan(l : A, r : A) : Boolean
    def transitivityLaw(a: A, b : A, c : A) : Boolean =
    lessThan(a,b) && lessThan(b,c)  == ???
  }

  implicit val LessThanInt : LessThan1[Int] =
    new LessThan1[Int] {
      def lessThan(l : Int, r : Int) : Boolean = l < r
    }


  //Typeclass rules
  //if you made the data type, put the instance is compainion class
  //if you didn't make the data type, but made the type class put the instance in typeclass

  /*
  A type class is a tuple of three things:

  1. A set of types and / or type constructors.
  2. A set of operations on values of those types.
  3. A set of laws governing the operations.

  A type class instance is an instance of a type class for a given
  set of types.
  */

  object hashmap {
    //- means input - contravariant
    //+ means output -
    trait Eq[A] {
      def eq(l : A, r: A) : Boolean
    }
    trait Hash[A] extends Eq[A]{
      def hash(a : A) : Int
    }

    object Hash {
      implicit val HashInt : Hash[Int] =
        new Hash[Int] {
          override def hash(a: Int): Int = ???

          override def eq(l: Int, r: Int): Boolean = ???
        }
    }

    class HashMap[K,V] {
        def insert(k : K, v : V): HashMap[K,V] = ???
      }
  }
  /**
   * All implementations are required to satisfy the transitivityLaw.
   *
   * Transitivity Law:
   * forall a b c.
   *   lt(a, b) && lt(b, c) ==
   *     lt(a, c) || (!lt(a, b) || !lt(b, c))
   */
  trait LessThan[A] {
    def lt(l: A, r: A): Boolean

    final def transitivityLaw(a: A, b: A, c: A): Boolean =
      (lt(a, b) && lt(b, c) == lt(a, c)) ||
      (!lt(a, b) || !lt(b, c))
  }
  implicit class LessThanSyntax[A](l: A) {
    def < (r: A)(implicit A: LessThan[A]): Boolean = A.lt(l, r)
    def >= (r: A)(implicit A: LessThan[A]): Boolean = !A.lt(l, r)
  }
  object LessThan {
    def apply[A](implicit A: LessThan[A]): LessThan[A] = A // summoner -> given the type, returns the instance of the typeclass for the instance

    implicit val LessThanInt: LessThan[Int] = new LessThan[Int] {
      def lt(l: Int, r: Int): Boolean = l < r
    }
    implicit def LessThanList[A: LessThan]: LessThan[List[A]] = new LessThan[List[A]] {
      def lt(l: List[A], r: List[A]): Boolean =
        (l, r) match {
          case (Nil, Nil) => false
          case (Nil, _) => true
          case (_, Nil) => false
          case (l :: ls, r :: rs) => l < r && lt(ls, rs)
        }
    }
  }

  //sort exists for all As where there exists a LessThan instance
  def sort[A: LessThan](l: List[A]): List[A] = l match {
    case Nil => Nil
    case x :: xs =>
      val (lessThan, notLessThan) = xs.partition(_ < x)

      sort(lessThan) ++ List(x) ++ sort(notLessThan)
  }

  sort(List(1, 2, 3))
  sort(List(List(1, 2, 3), List(9, 2, 1), List(1, 2, 9)))
}

object typeclasses {
  /**
   * {{
   * Reflexivity:   a ==> equals(a, a)
   *
   * Transitivity:  equals(a, b) && equals(b, c) ==>
   *                equals(a, c)
   *
   * Symmetry:      equals(a, b) ==> equals(b, a)
   * }}
   */
  trait Eq[A] {
    def equals(l: A, r: A): Boolean
  }
  object Eq {
    def apply[A](implicit eq: Eq[A]): Eq[A] = eq

    implicit val EqInt: Eq[Int] = new Eq[Int] {
      def equals(l: Int, r: Int): Boolean = l == r
    }
    implicit def EqList[A: Eq]: Eq[List[A]] =
      new Eq[List[A]] {
        def equals(l: List[A], r: List[A]): Boolean =
          (l, r) match {
            case (Nil, Nil) => true
            case (Nil, _) => false
            case (_, Nil) => false
            case (l :: ls, r :: rs) =>
              Eq[A].equals(l, r) && equals(ls, rs)
          }
      }
  }
  implicit class EqSyntax[A](val l: A) extends AnyVal {
    def === (r: A)(implicit eq: Eq[A]): Boolean =
      eq.equals(l, r)
  }

  //
  // Scalaz 7 Encoding
  //
  sealed trait Ordering
  case object EQUAL extends Ordering
  case object LT extends Ordering
  case object GT extends Ordering
  object Ordering {
    implicit val OrderingEq: Eq[Ordering] = new Eq[Ordering] {
      def equals(l: Ordering, r: Ordering): Boolean =
        (l, r) match {
          case (EQUAL, EQUAL) => true
          case (LT, LT) => true
          case (GT, GT) => true
          case _ => false
        }
    }
  }

  trait Ord[A] {
    def compare(l: A, r: A): Ordering

    final def eq(l: A, r: A): Boolean = compare(l, r) == EQUAL
    final def lt(l: A, r: A): Boolean = compare(l, r) == LT
    final def lte(l: A, r: A): Boolean = lt(l, r) || eq(l, r)
    final def gt(l: A, r: A): Boolean = compare(l, r) == GT
    final def gte(l: A, r: A): Boolean = gt(l, r) || eq(l, r)

    final def transitivityLaw1(a: A, b: A, c: A): Boolean =
      (lt(a, b) && lt(b, c) == lt(a, c)) ||
      (!lt(a, b) || !lt(b, c))

    final def transitivityLaw2(a: A, b: A, c: A): Boolean =
      (gt(a, b) && gt(b, c) == gt(a, c)) ||
      (!gt(a, b) || !gt(b, c))

    final def equalityLaw(a: A, b: A): Boolean =
      (lt(a, b) && gt(a, b) == eq(a, b)) ||
      (!lt(a, b) || !gt(a, b))
  }
  object Ord {
    def apply[A](implicit A: Ord[A]): Ord[A] = A

    implicit val OrdInt: Ord[Int] = new Ord[Int] {
      def compare(l: Int, r: Int): Ordering =
        if (l < r) LT else if (l > r) GT else EQUAL
    }
  }
  implicit class OrdSyntax[A](val l: A) extends AnyVal {
    def =?= (r: A)(implicit A: Ord[A]): Ordering =
      A.compare(l, r)

    def < (r: A)(implicit A: Ord[A]): Boolean =
      Eq[Ordering].equals(A.compare(l, r), LT)

    def <= (r: A)(implicit A: Ord[A]): Boolean =
      (l < r) || (this === r)

    def > (r: A)(implicit A: Ord[A]): Boolean =
      Eq[Ordering].equals(A.compare(l, r), GT)

    def >= (r: A)(implicit A: Ord[A]): Boolean =
      (l > r) || (this === r)

    def === (r: A)(implicit A: Ord[A]): Boolean =
      Eq[Ordering].equals(A.compare(l, r), EQUAL)

    def !== (r: A)(implicit A: Ord[A]): Boolean =
      !Eq[Ordering].equals(A.compare(l, r), EQUAL)
  }
  case class Person(age: Int, name: String)
  object Person {
    implicit val OrdPerson: Ord[Person] = new Ord[Person] {
      def compare(l: Person, r: Person): Ordering =
        if (l.age < r.age) LT else if (l.age > r.age) GT
        else if (l.name < r.name) LT else if (l.name > r.name) GT
        else EQUAL
    }
    implicit val EqPerson: Eq[Person] = new Eq[Person] {
      def equals(l: Person, r: Person): Boolean =
        l == r
    }
  }

  //
  // EXERCISE 1
  //
  // Write a version of `sort1` called `sort2` that uses the polymorphic `List`
  // type, and which uses the `Ord` type class, including the compare syntax
  // operator `=?=` to compare elements.
  //
  def sort1(l: List[Int]): List[Int] = l match {
    case Nil => Nil
    case x :: xs =>
      val (lessThan, notLessThan) = xs.partition(_ < x)

      sort1(lessThan) ++ List(x) ++ sort1(notLessThan)
  }
  def sort2[A: Ord](l: List[A]): List[A] = l match {
    case Nil => Nil
    case x :: xs =>
      val (lessThan, notLessThan) = xs.partition(_ < x)

      sort2(lessThan) ++ List(x) ++ sort2(notLessThan)
  }

  //
  // EXERCISE 2
  //
  // Create a data structure and an instance of this type class for the data
  // structure.
  //
  trait PathLike[A] {
    def child(parent: A, name: String): A

    def parent(node: A): Option[A]

    def root: A
  }
  object PathLike {
    def apply[A](implicit A: PathLike[A]): PathLike[A] = A
  }
  case class MyPath(name : String, children : List[MyPath])

  implicit val MyPathPathLike: PathLike[MyPath] =
    new PathLike[MyPath] {
      def child(parent: MyPath, name: String): MyPath = ???
      def parent(node: MyPath): Option[MyPath] = ???
      def root: MyPath = ???
    }

  //
  // EXERCISE 3
  //
  // Create an instance of the `PathLike` type class for `java.io.File`.
  //
  implicit val FilePathLike: PathLike[java.io.File] = new PathLike[java.io.File] {
    override def child(parent: File, name: String): File = parent.list().filter(_ == name).headOption.map(x => new File(x)).getOrElse(new File(""))

    override def parent(node: File): Option[File] = Option(node.getParentFile)

    override def root: File = new File("/")
  }

  //
  // EXERCISE 4
  //
  // Create two laws for the `PathLike` type class.
  //
  trait PathLikeLaws[A] extends PathLike[A] {
    def law1: Boolean = parent(root) == None

    def law2(node: A, name: String, assertEquals: (A, A) => Boolean): Boolean =
      parent(child(node,name)).fold(false)(assertEquals(node,_))
  }

  //
  // EXERCISE 5
  //
  // Create a syntax class for path-like values with a `/` method that descends
  // into the given named node.
  //
  implicit class PathLikeSyntax[A](a: A) {
    def / (name: String)(implicit A: PathLike[A]): A = A.child(a,name)

    def parent(implicit A: PathLike[A]): Option[A] = A.parent(a)
  }
  def root[A: PathLike]: A = PathLike[A].root
  //  root[MyPath] / "foo" / "bar" / "baz" // MyPath
  // (root[MyPath] / "foo").parent        // Option[MyPath]

  //
  // EXERCISE 6
  //
  // Create an instance of the `Filterable` type class for `List`.
  //
  trait Filterable[F[_]] {
    def filter[A](fa: F[A], f: A => Boolean): F[A]
  }
  object Filterable {
    def apply[F[_]](implicit F: Filterable[F]): Filterable[F] = F
  }
  implicit val FilterableList: Filterable[List] = new Filterable[List] {
    override def filter[A](fa: List[A], f: A => Boolean): List[A] = fa.filter(f)
  }

  //
  // EXERCISE 7
  //
  // Create a syntax class for `Filterable` that lets you call `.filterWith` on any
  // type for which there exists a `Filterable` instance.
  //
  implicit class FilterableSyntax[F[_], A](fa: F[A]) {
//    def filterWith(f : A => Boolean)(implicit A: Filterable[A]) = A.filter(fa, f)
  }
  // List(1, 2, 3).filterWith(_ == 2)

  //
  // EXERCISE 8
  //
  // Create an instance of the `Collection` type class for `List`.
  //
  trait Collection[F[_]] {
    def empty[A]: F[A]
    def cons[A](a: A, as: F[A]): F[A]
    def uncons[A](fa: F[A]): Option[(A, F[A])]
  }
  object Collection {
    def apply[F[_]](implicit F: Collection[F]): Collection[F] = F
  }
  implicit val ListCollection: Collection[List] = ???

  val example = Collection[List].cons(1, Collection[List].empty)

  //
  // EXERCISE 9
  //
  // Create laws for the `Collection` type class.
  //
  trait CollectionLaws[F[_]] extends Collection[F] {

  }

  //
  // EXERCISE 10
  //
  // Create syntax for values of any type that has `Collection` instances.
  // Specifically, add an `uncons` method to such types.
  //
  implicit class CollectionSyntax[F[_], A](fa: F[A]) {
    ???

    def cons(a: A)(implicit F: Collection[F]): F[A] = F.cons(a, fa)
  }
  def empty[F[_]: Collection, A]: F[A] = Collection[F].empty[A]
  // List(1, 2, 3).uncons // Some((1, List(2, 3)))
}

//fp game
// write the result first, and then insert holes
// use functions to move us from inputs to end state

//comparable in java is basically a typeclass
