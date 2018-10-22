// Copyright(C) 2018 - John A. De Goes. All rights reserved.

package net.degoes.abstractions

import scalaz._
import Scalaz._

object algebra {
  //
  // EXERCISE 1
  //
  // Define a semigroup for `NotEmpty` below.
  //
  case class NotEmpty[+A](head: A, tail: Option[NotEmpty[A]])
  implicit def NotEmptySemigroup[A]: Semigroup[NotEmpty[A]] =
    ???
  val example1 = NotEmpty(1, None) |+| NotEmpty(2, None)

  //
  // EXERCISE 2
  //
  // Define a semigroup for `Max` that chooses the maximum of two values.
  //
  final case class Max(value: Int)
  implicit val MaxSemigroup: Semigroup[Max] = ???

  //
  // EXERCISE 3
  //
  // Define a `Semigroup` for `Last[A]` that always chooses the right-most value.
  //
  final case class Last[A](value: A)
  implicit def LastSemigroup[A]: Semigroup[Last[A]] =
    new Semigroup[Last[A]] {
      def append(l: Last[A], r: => Last[A]): Last[A] = r
    }

  //
  // EXERCISE 4
  //
  // Define a `Semigroup` for `Option[A]` whenever `A` forms a `Semigroup`.
  //
  implicit def OptionSemigroup[A: Semigroup]: Semigroup[Option[A]] = ???

  //
  // EXERCISE 5
  //
  // Define an instance of `Semigroup` for `(A, B)` when both `A` and
  // `B` form semigroups.
  //
  implicit def SemigroupTuple2[A: Semigroup, B: Semigroup]:
    Semigroup[(A, B)] = new Semigroup[(A, B)] {
      def append(l: (A, B), r: => (A, B)): (A, B) =
        ???
    }

  //
  // EXERCISE 6
  //
  // Define a monoid for boolean conjunction (`&&`).
  //
  final case class Conj(value: Boolean)
  implicit val ConjMonoid: Monoid[Conj] = ???

  //
  // EXERCISE 7
  //
  // Define a `Monoid` for `Option[A]` whenever `A` forms a `Semigroup`.
  //
  def OptionMonoid[A: Semigroup]: Monoid[Option[A]] = ???

  //
  // EXERCISE 8
  //
  // Write the `Monoid` instance for `Map`.
  //
  def SemigroupMap[K, V: Semigroup]: Semigroup[Map[K, V]] = ???

  //
  // EXERCISE 9
  //
  // Design a permission system for securing some resource, together with a
  // monoid for the permission data structure.
  //
  // Assumptions:
  //   1. Users have multiple accounts (`AccountID`)
  //   2. Each account gives different capabilities (`Capability`) to
  //      different resources (`ResourceID`)
  //
  type AccountID = String
  type ResourceID = String
  sealed trait Capability
  object Capability {
    final case object Read extends Capability
    final case object Write extends Capability
  }
  case class UserPermission(/* */)
  implicit val MonoidUserPermission: Monoid[UserPermission] = ???
  val example2 = mzero[UserPermission] |+| UserPermission(/* */)

  //
  // EXERCISE 10
  //
  // Try to define an instance of `Monoid` for `NotEmpty` for any type `A`.
  //
  implicit def MonoidNotEmpty[A]: Monoid[NotEmpty[A]] = ???
}

object functor {
  /**
   * Identity Law
   *   map(fa, identity) == fa
   *
   * Composition Law
   *   map(map(fa, g), f) == map(fa, f.compose(g))
   */

  val Numbers  = List(12, 123, 0, 123981)
  val Expected = List( 2,   3, 1,      6)
  val g : Int => String = (i: Int) => i.toString
  val f : String => Int = (s: String) => s.length
  Numbers.map(identity)    == Numbers
  Numbers.map(g andThen f) == Numbers.map(g).map(f)

  //
  // EXERCISE 1
  //
  // Define an instance of `Functor` for `BTree`.
  //
  sealed trait BTree[+A]
  case class Leaf[A](a: A) extends BTree[A]
  case class Fork[A](left: BTree[A], right: BTree[A]) extends BTree[A]
  implicit val BTreeFunctor: Functor[BTree] =
    new Functor[BTree] {
      def map[A, B](fa: BTree[A])(f: A => B): BTree[B] =
        ???
    }

  //
  // EXERCISE 2
  //
  // Define an instance of `Functor` for `Nothing`.
  //
  implicit val NothingFunctor: Functor[Nothing] = ???

  //
  // EXERCISE 3
  //
  // Define an instance of `Functor` for Parser[E, ?].
  //
  case class Parser[+E, +A](run: String => Either[E, (String, A)])
  object Parser {
    def fail[E](e: E): Parser[E, Nothing] =
      Parser(input => Left(e))

    def point[A](a: => A): Parser[Nothing, A] =
      Parser(input => Right((input, a)))

    def char[E](e: E): Parser[E, Char] =
      Parser(input =>
        if (input.length == 0) Left(e)
        else Right((input.drop(1), input.charAt(0))))

    implicit def ParserFunctor[E]: Functor[Parser[E, ?]] =
      new Functor[Parser[E, ?]] {
        def map[A, B](fa: Parser[E, A])(f: A => B): Parser[E, B] =
           ???
      }
  }

  //
  // EXERCISE 4
  //
  // Try to define an instance of `Functor` for the following data type.
  //
  case class DataType[A](f: A => A)
  implicit val DataTypeFunctor: Functor[DataType] =
    new Functor[DataType] {
      def map[A, B](fa: DataType[A])(f: A => B): DataType[B] =
        ???
    }

  //
  // EXERCISE 5
  //
  // Define an instance of `Functor` for `FunctorProduct`.
  //
  case class FunctorProduct[F[_], G[_], A](l: F[A], r: G[A])
  implicit def FunctorProductFunctor[F[_]: Functor, G[_]: Functor]:
    Functor[FunctorProduct[F, G, ?]] =
      new Functor[FunctorProduct[F, G, ?]] {
        def map[A, B](fa: FunctorProduct[F, G, A])(f: A => B): FunctorProduct[F, G, B] =
          ???
      }

  //
  // EXERCISE 6
  //
  // Define an instance of `Functor` for `FunctorSum`.
  //
  case class FunctorSum[F[_], G[_], A](run: Either[F[A], G[A]])
  implicit def FunctorSumFunctor[F[_]: Functor, G[_]: Functor]:
    Functor[FunctorSum[F, G, ?]] =
      new Functor[FunctorSum[F, G, ?]] {
        def map[A, B](fa: FunctorSum[F, G, A])(f: A => B): FunctorSum[F, G, B] =
          ???
      }

  //
  // EXERCISE 7
  //
  // Define an instance of `Functor` for `FunctorNest`.
  // e.g. Future[Option[A]], Future[Either[Error, Option[A]]]
  // Future[List[Either[Error, Option[A]]]]
  //
  case class FunctorNest[F[_], G[_], A](run: F[G[A]])
  implicit def FunctorNestFunctor[F[_]: Functor, G[_]: Functor]:
    Functor[FunctorNest[F, G, ?]] =
      new Functor[FunctorNest[F, G, ?]] {
        def map[A, B](fa: FunctorNest[F, G, A])(f: A => B): FunctorNest[F, G, B] =
          ???
      }

  //
  // EXERCISE 8
  //
  // Define an instance of `Zip` for `Option`.
  //
  trait Zip[F[_]] extends Functor[F] {
    def zip[A, B](l: F[A], r: F[B]): F[(A, B)]
  }
  object Zip {
    implicit val ZipOption: Zip[Option] =
      new Zip[Option] {
        def map[A, B](fa: Option[A])(f: A => B) = fa.map(f)

        def zip[A, B](l: Option[A], r: Option[B]): Option[(A, B)] =
          ???
      }
  }
  implicit class ZipSyntax[F[_], A](left: F[A]) {
    def zip[B](right: F[B])(implicit F: Zip[F]): F[(A, B)] =
      F.zip(left, right)
  }

  //
  // EXERCISE 9
  //
  // Define an instance of `Zip` for `List`
  //
  val ZipList: Zip[List] =
    new Zip[List] {
      def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)

      def zip[A, B](l: List[A], r: List[B]): List[(A, B)] = ???
    }

  //
  // EXERCISE 10
  //
  // Define an instance of `Zip` for `Parser[E, ?]`.
  //
  def ZipParser[E]: Zip[Parser[E, ?]] =
    new Zip[Parser[E, ?]] {
      def map[A, B](fa: Parser[E, A])(f: A => B): Parser[E, B] =
        Parser.ParserFunctor.map(fa)(f)

      def zip[A, B](l: Parser[E, A], r: Parser[E, B]): Parser[E, (A, B)] = ???
    }

  //
  // EXERCISE 11
  //
  // Define an instance of `Zip` for `Future`.
  //
  val ZipFuture: Zip[scala.concurrent.Future] = ???

  //
  // EXERCISE 12
  //
  // Define `Applicative` for `Option`.
  //
  val OptionApplicative: Applicative[Option] =
    new Applicative[Option] {
      def point[A](a: => A): Option[A] = ???

      def ap[A, B](fa: => Option[A])(f: => Option[A => B]): Option[B] = ???
    }

  //
  // EXERCISE 13
  //
  // Implement `zip` in terms of the applicative composition using `|@|`.
  //
  // Bonus: Implement `ap2` in terms of `zip`.
  //
  val example1 = (Option(3) |@| Option(5))(_ + _)
  val example2 = zip(Option(3), Option("foo")) : Option[(Int, String)]
  def zip[F[_]: Applicative, A, B](l: F[A], r: F[B]): F[(A, B)] =
    ???
  def ap2[F[_]: Zip, A, B](fa: F[A], fab: F[A => B]): F[B] =
    ???

  //
  // EXERCISE 14
  //
  // Define an instance of `Applicative` for `Parser[E, ?]`.
  //
  def ApplicativeParser[E]: Applicative[Parser[E, ?]] =
    new Applicative[Parser[E, ?]] {
      def point[A](a: => A): Parser[E,A] =
        ???

      def ap[A, B](fa: => Parser[E,A])(
        f: => Parser[E, A => B]): Parser[E,B] =
          ???
    }

  //
  // EXERCISE 15
  //
  // Define an instance of `Monad` for `BTree`.
  //
  implicit val MonadBTree: Monad[BTree] =
    new Monad[BTree] {
      def point[A](a: => A): BTree[A] =
        ???

      def bind[A, B](fa: BTree[A])(f: A => BTree[B]): BTree[B] =
        ???
    }

  //
  // EXERCISE 16
  //
  // Define an instance of `Monad` for `Parser[E, ?]`.
  //
  implicit def MonadParser[E]: Monad[Parser[E, ?]] =
    new Monad[Parser[E, ?]] {
      def point[A](a: => A): Parser[E,A] = ???

      def bind[A, B](fa: Parser[E,A])(f: A => Parser[E,B]): Parser[E,B] =
        ???
    }
}

object parser {
  //
  // EXERCISE 1
  //
  // Implement all missing methods for parser.
  //
  case class Parser[+E, +A](run: String => Either[E, (String, A)]) { self =>
    def ~ [E1 >: E, B](that: => Parser[E1, B]): Parser[E1, (A, B)] =
      self.flatMap(a => that.map(b => (a, b)))

    def ~> [E1 >: E, B](that: => Parser[E1, B]): Parser[E1, B] =
      (self ~ that).map(_._2)

    def <~ [E1 >: E, B](that: => Parser[E1, B]): Parser[E1, A] =
      (self ~ that).map(_._1)

    def map[B](f: A => B): Parser[E, B] =
      flatMap(f.andThen(Parser.point[B](_)))

    def flatMap[E1 >: E, B](f: A => Parser[E1, B]): Parser[E1, B] =
      Parser[E1, B](input =>
        self.run(input) match {
          case Left(e) => Left(e)
          case Right((input, a)) => f(a).run(input)
        })

    def orElse[E1 >: E, B](that: => Parser[E1, B]): Parser[E1, Either[A, B]] = {
      val self1 = self.map(Left(_))
      val that1 = that.map(Right(_))

      type Return = Either[E1, (String, Either[A, B])]

      Parser(i => self1.run(i).fold[Return](_ => that1.run(i), Right(_)))
    }

    def filter[E1 >: E](e0: E1)(f: A => Boolean): Parser[E1, A] =
      Parser(input =>
        self.run(input) match {
          case Left(e) => Left(e)
          case Right((input, a)) => if (f(a)) Right((input, a)) else Left(e0)
        })

    def | [E1 >: E, A1 >: A](that: => Parser[E1, A1]): Parser[E1, A1] =
      (self orElse (that)).map(_.merge)

    def rep: Parser[E, List[A]] =
      ((self.map(List(_)) | Parser.point[List[A]](Nil)) ~ rep).map(t => t._1 ++ t._2)

    def ? : Parser[E, Option[A]] = self.map(Some(_)) | Parser.point(None)
  }
  object Parser {
    def fail[E](e: E): Parser[E, Nothing] =
      Parser(input => Left(e))

    def point[A](a: => A): Parser[Nothing, A] =
      Parser(input => Right((input, a)))

    def maybeChar: Parser[Nothing, Option[Char]] =
      Parser(input =>
        if (input.length == 0) Right((input, None))
        else Right((input.drop(1), Some(input.charAt(0)))))

    def char[E](e: E): Parser[E, Char] =
      Parser(input =>
        if (input.length == 0) Left(e)
        else Right((input.drop(1), input.charAt(0))))

    def digit[E](e: E): Parser[E, Int] =
      for {
        c <- char(e)
        option = scala.util.Try(c.toString.toInt).toOption
        d <- option.fold[Parser[E, Int]](Parser.fail(e))(point(_))
      } yield d

    def literal[E](f: Char => E)(c0: Char): Parser[E, Char] =
      for {
        c <- char(f(c0))
        _ <- if (c != c0) Parser.point(f(0)) else Parser.point(())
      } yield c

    def whitespace: Parser[Nothing, Unit] =
      Parser(input => Right((input.dropWhile(_ == ' '), ())))
  }

  // [1,2,3,]
  sealed trait Error
  case class ExpectedLit(char: Char) extends Error
  case object ExpectedDigit extends Error

  val parser: Parser[Error, List[Int]] =
    for {
      _       <- Parser.literal(ExpectedLit)('[')
      digits  <- (Parser.digit(ExpectedDigit) <~ Parser.literal(ExpectedLit)(',')).rep
      _       <- Parser.literal(ExpectedLit)(']')
    } yield digits
}

object foldable {
  //
  // EXERCISE 0
  //
  // Define an instance of `Foldable` for `List`
  //
  implicit val FoldableList: Foldable[List] = new Foldable[List] {
    def foldMap[A, B: Monoid](fa: List[A])(f: A => B): B =
      fa match {
        case Nil => mzero[B]
        case a :: as => f(a) |+| foldMap(as)(f)
      }

    def foldRight[A, B](fa: List[A], z: => B)(f: (A, => B) => B): B =
      fa match {
        case Nil => z
        case a :: as => f(a, foldRight(as, z)(f))
      }
  }

  //
  // EXERCISE 1
  //
  // Define an instance of `Foldable` for `BTree`.
  //
  sealed trait BTree[+A]
  case class Leaf[A](a: A) extends BTree[A]
  case class Fork[A](left: BTree[A], right: BTree[A]) extends BTree[A]
  implicit val FoldableBTree: Foldable[BTree] =
    new Foldable[BTree] {
      def foldMap[A, B](fa: BTree[A])(f: A => B)(implicit F: Monoid[B]): B =
        ???

      def foldRight[A, B](fa: BTree[A], z: => B)(f: (A, => B) => B): B =
        ???
    }

  //
  // EXERCISE 2
  //
  // Try to define an instance of `Foldable` for `A0 => ?`.
  //
  implicit def FunctionFoldable[A0]: Foldable[A0 => ?] = ???

  //
  // EXERCISE 3
  //
  // Define an instance of `Traverse` for `BTree`.
  //
  implicit val TraverseBTree: Traverse[BTree] =
    new Traverse[BTree] {
      def traverseImpl[G[_]: Applicative, A, B](
        fa: BTree[A])(f: A => G[B]): G[BTree[B]] = ???
    }

  //
  // EXERCISE 4
  //
  // Try to define an instance of `Traverse` for `Parser[E, ?]`.
  //
  case class Parser[+E, +A](run: String => Either[E, (String, A)])
  implicit def TraverseParser[E]: Traverse[Parser[E, ?]] =
    new Traverse[Parser[E, ?]] {
       def traverseImpl[G[_]: Applicative, A, B](fa: Parser[E, A])(f: A => G[B]): G[Parser[E,B]] =
         ???
    }
}

object optics {
  sealed trait Country
  object Country {
    val usa: Prism[Country, Unit] = ???
  }
  case object USA    extends Country
  case object UK     extends Country
  case object Poland extends Country

  case class Org(name: String, address: Address, site: Site)
  object Org {
    val site: Lens[Org, Site] = ???
  }

  case class Address(
    number: String,
    street: String,
    postalCode: String,
    country: Country)

  case class Site(
    manager: Employee,
    address: Address,
    employees: Set[Employee])
  object Site {
    val manager: Lens[Site, Employee] = ???
  }
  case class Employee(
    name: String,
    dob: java.time.Instant,
    salary: BigDecimal,
    address: Address)
  object Employee {
    val salary: Lens[Employee, BigDecimal] = ???
  }

  lazy val org: Org = ???

  lazy val org2 =
    org.copy(site =
      org.site.copy(manager = org.site.manager.copy(
        salary = org.site.manager.salary * 0.95
      ))
    )

  //
  // EXERCISE 1
  //
  // Implement the `⋅` method of `Lens`.
  //
  final case class Lens[S, A](
    get: S => A,
    set: A => (S => S)
  ) { self =>
    def ⋅ [B](that: Lens[A, B]): Lens[S, B] = ???

    final def updated(f: A => A): S => S =
      (s: S) => self.set(f(self.get(s)))(s)
  }

  //
  // EXERCISE 2
  //
  // Create a version of `org2` that uses lenses to update the salaries.
  //
  import Org.site
  import Site.manager
  import Employee.salary
  val org2_lens: Org = ???

  //
  // EXERCISE 3
  //
  // Implement `⋅` for `Prism`.
  //
  final case class Prism[S, A](
    get: S => Option[A],
    set: A => S) { self =>
    def ⋅ [B](that: Prism[A, B]): Prism[S, B] = ???

    final def select(implicit ev: Unit =:= A): S =
      set(ev(()))
  }

  //
  // EXERCISE 4
  //
  // Implement `_Left` and `_Right`.
  //
  def _Left[A, B]: Prism[Either[A, B], A] =
    ???
  def _Right[A, B]: Prism[Either[A, B], B] =
    ???
}
