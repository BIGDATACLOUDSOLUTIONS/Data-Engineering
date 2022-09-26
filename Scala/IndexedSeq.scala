Create a new IndexedSeq with initial elements:

Create an IndexedSeq:
val nums = IndexedSeq(1, 2, 3)          // IndexedSeq[Int] = Vector(1, 2, 3)
val words = IndexedSeq("foo", "bar")    // IndexedSeq[String] = Vector(foo, bar)


When the values in the sequence have mixed/multiple types you may want to specify the type of the sequence:
val x = IndexedSeq(1, 1.0, 1F)                       // IndexedSeq[Double] = IndexedSeq(1.0, 1.0, 1.0)
val x: IndexedSeq[Number] = IndexedSeq(1, 1.0, 1F)   // IndexedSeq[Number] = Vector(1, 1.0, 1.0)

If you ever need to create an empty IndexedSeq:
val nums = IndexedSeq[Int]()            // IndexedSeq[Int] = Vector()

Remember the construction syntax is just syntactic sugar for apply:
val nums = IndexedSeq(1, 2, 3)          // IndexedSeq[Int] = Vector(1, 2, 3)
val nums = IndexedSeq.apply(1, 2, 3)    // IndexedSeq[Int] = Vector(1, 2, 3)


Create a new IndexedSeq by populating it
You can create a new IndexedSeq that’s populated with initial elements using a Range:
# to, until
(1 to 5).toIndexedSeq                   // IndexedSeq[Int] = Range 1 to 5
(1 until 5).toIndexedSeq                // IndexedSeq[Int] = Range 1 to 5

(1 to 10 by 2).toIndexedSeq             // IndexedSeq[Int] = inexact Range 1 to 10 by 2
(1 until 10 by 2).toIndexedSeq          // IndexedSeq[Int] = inexact Range 1 until 10 by 2
(1 to 10).by(2).toIndexedSeq            // IndexedSeq[Int] = inexact Range 1 to 10 by 2

('d' to 'h').toIndexedSeq               // IndexedSeq[Char] = NumericRange d to h
('d' until 'h').toIndexedSeq            // IndexedSeq[Char] = NumericRange d until h

('a' to 'f').by(2).toIndexedSeq         // IndexedSeq[Char] = NumericRange a to f by ?

# range method
IndexedSeq.range(1, 3)                  // IndexedSeq[Int] = Vector(1, 2)
IndexedSeq.range(1, 6, 2)               // IndexedSeq[Int] = Vector(1, 3, 5)


You can also use the fill and tabulate methods:
IndexedSeq.fill(3)("foo")          // IndexedSeq[String] = Vector(foo, foo, foo)
IndexedSeq.tabulate(3)(n => n * n)      // IndexedSeq[Int] = Vector(0, 1, 4)
IndexedSeq.tabulate(4)(n => n * n)      // IndexedSeq[Int] = Vector(0, 1, 4, 9)
val h=IndexedSeq.tabulate(3,4)(_*_)    // IndexedSeq[IndexedSeq[Int]] = Vector(Vector(0, 0, 0, 0), Vector(0, 1, 2, 3), Vector(0, 2, 4, 6))


How to add (append and prepend) elements to a IndexedSeq
Because IndexedSeq is immutable, you can’t add elements to an existing IndexedSeq. The way you work with IndexedSeq is to modify
the elements it contains as you assign the results to a new IndexedSeq.

Method	  Description	          Example
:+	--> append 1 item	  --> oldIndexedSeq :+ e
++	--> append N items  -->	oldIndexedSeq ++ newIndexedSeq
+:	--> prepend 1 item  -->	e +: oldIndexedSeq
++:	--> prepend N items --> newIndexedSeq ++: oldIndexedSeq

Again, you can use these methods, but it’s not recommended.

Append and prepend examples
These examples show how to use those methods to append and prepend elements to an IndexedSeq:
val v1 = IndexedSeq(4,5,6)         // IndexedSeq[Int] = Vector(4, 5, 6)
val v2 = v1 :+ 7                   // Vector(4, 5, 6, 7)
val v3 = v2 ++ IndexedSeq(8,9)     // Vector(4, 5, 6, 7, 8, 9)

val v4 = 3 +: v3                   // Vector(3, 4, 5, 6, 7, 8, 9)
val v5 = IndexedSeq(1,2) ++: v4    // Vector(1, 2, 3, 4, 5, 6, 7, 8, 9)

About the : character in the method names
Note that during these operations the : character is always next to the old (original) sequence. I use that as a way to remember these 
methods. The correct technical way to think about this is that a Scala method name that ends with the : character is right-associative, 
meaning that the method comes from the variable on the right side of the expression. 
Therefore, with +: and ++:, these methods comes from the IndexedSeq that’s on the right of the method name.

Filtering methods (how to “remove” elements from a IndexedSeq):
A IndexedSeq is an immutable sequence, so you don’t remove elements from it. Instead, you describe how to remove elements as you assign the 
results to a new collection. These methods let you “remove” elements during this process:


Method	Description
distinct --> Return a new sequence with no duplicate elements
drop(n) --> Return all elements after the first n elements
dropRight(n) -->	Return all elements except the last n elements
dropWhile(p) -->	Drop the first sequence of elements that matches the predicate p
filter(p) -->	Return all elements that match the predicate p
filterNot(p) -->	Return all elements that do not match the predicate p
find(p) -->	Return the first element that matches the predicate p
head -->	Return the first element; can throw an exception if the IndexedSeq is empty
headOption -->	Returns the first element as an Option
init -->	All elements except the last one
intersect(s) -->	Return the intersection of the sequence and another sequence s
last -->	The last element; can throw an exception if the IndexedSeq is empty
lastOption -->	The last element as an Option
slice(f,u) -->	A sequence of elements from index f (from) to index u (until) tail	All elements after the first element
take(n) -->	The first n elements
takeRight(n) -->	The last n elements
takeWhile(p) -->	The first subset of elements that matches the predicate p

Note:
dropWhile -->	discards all the items at the start of a collection for which the condition is true. It stops discarding as soon as 
the first item fails the condition.
filter --> discards all the items throughout the collection where the condition is not true. It does not stop until the end of the collection.

Examples:
val a = IndexedSeq(10, 20, 30, 40, 10)   // Vector(10, 20, 30, 40, 10)
a.distinct                            // Vector(10, 20, 30, 40)
a.drop(2)                             // Vector(30, 40, 10)
a.dropRight(2)                        // Vector(10, 20, 30)
a.dropWhile(_ < 25)                   // Vector(30, 40, 10)
a.filter(_ < 25)                      // Vector(10, 20, 10)
a.filter(_ > 100)                     // Vector()
a.filterNot(_ < 25)                   // Vector(30, 40)
a.find(_ > 20)                        // Some(30)
a.head                                // 10
a.headOption                          // Some(10)
a.init                                // Vector(10, 20, 30, 40)
a.intersect(IndexedSeq(19,20,21))     // Vector(20)
a.last                                // 10
a.lastOption                          // Some(10)
a.slice(2,4)                          // Vector(30, 40)
a.tail                                // Vector(20, 30, 40, 10)
a.take(3)                             // Vector(10, 20, 30)
a.takeRight(2)                        // Vector(40, 10)
a.takeWhile(_ < 30)                   // Vector(10, 20)


As noted, head and last can throw exceptions:
scala> val a = IndexedSeq[Int]()
a: IndexedSeq[Int] = Vector()

scala> a.head
java.lang.UnsupportedOperationException: empty.head
  at scala.collection.immutable.Vector.head(Vector.scala:185)
  ... 28 elided

scala> a.last
java.lang.UnsupportedOperationException: empty.last
  at scala.collection.immutable.Vector.last(Vector.scala:197)
  ... 28 elided



How to "update" IndexedSeq elements
Because IndexedSeq is immutable, you can’t update elements in place, but depending on your definition of "update", there are a variety of 
methods that let you update a IndexedSeq as you assign the result to a new variable:

collect(pf) --> A new collection by applying the partial function pf to all elements of the sequence, returning elements for which the 
                function is defined
distinct -->	A new sequence with no duplicate elements
flatten -->	Transforms a sequence of sequences into a single sequence
flatMap(f) -->	When working with sequences, it works like map followed by flatten
map(f) -->	Return a new sequence by applying the function f to each element in the IndexedSeq
updated(i,v) -->	A new sequence with the element at index i replaced with the new value v
union(s) -->	A new sequence that contains elements from the current sequence and the sequence s

val x = IndexedSeq(Some(1), None, Some(3), None)   // IndexedSeq[Option[Int]] = Vector(Some(1), None, Some(3), None)

x.collect{case Some(i) => i}              // IndexedSeq(1, 3)
//For More Use Cases of "Collect", read @ https://medium.com/@sergigp/using-scala-collect-3a9880f71e23

val x = IndexedSeq(1,2,1,2)
x.distinct                                // Vector(1, 2)
x.map(_ * 2)                              // Vector(2, 4, 2, 4)
x.updated(0,100)                          // Vector(100, 2, 1, 2)

val a = IndexedSeq(IndexedSeq(1,2), IndexedSeq(3,4))
a.flatten                                 // Vector(1, 2, 3, 4)

val fruits = IndexedSeq("apple", "pear")
fruits.map(_.toUpperCase)                 // Vector(APPLE, PEAR)
fruits.flatMap(_.toUpperCase)             // Vector(A, P, P, L, E, P, E, A, R)

IndexedSeq(2,4).union(IndexedSeq(1,3))    // Vector(2, 4, 1, 3)

//Question: What is difference between in flapMap and flatten Methods?

Transformer methods
A transformer method is a method that constructs a new collection from an existing collection.

collect(pf) --> Creates a new collection by applying the partial function pf to all elements of the sequence, returning elements for 
                which the function is defined
diff(c) -->	The difference between this sequence and the collection c
distinct -->	A new sequence with no duplicate elements
flatten -->	Transforms a sequence of sequences into a single sequence
flatMap(f) -->	When working with sequences, it works like map followed by flatten
map(f) -->	A new sequence by applying the function f to each element in the IndexedSeq
reverse -->	A new sequence with the elements in reverse order
sortWith(f) -->	A new sequence with the elements sorted with the use of the function f
updated(i,v) -->	A new IndexedSeq with the element at index i replaced with the new value v
union(c) -->	A new sequence that contains all elements of the sequence and the collection c
zip(c) -->	A collection of pairs by matching the sequence with the elements of the collection c
zipWithIndex -->	A sequence of each element contained in a tuple along with its index

val x = IndexedSeq(Some(1), None, Some(3), None)

x.collect{case Some(i) => i}                // Vector(1, 3)

# diff
val oneToFive = (1 to 5).toIndexedSeq       // IndexedSeq[Int] = Range 1 to 5
val threeToSeven = (3 to 7).toIndexedSeq    // IndexedSeq[Int] = Range 3 to 7
oneToFive.diff(threeToSeven)                // IndexedSeq[Int] = Vector(1, 2)
threeToSeven.diff(oneToFive)                // IndexedSeq[Int] = Vector(6, 7)

IndexedSeq(1,2,1,2).distinct                // IndexedSeq[Int] = Vector(1, 2)

val a = IndexedSeq(IndexedSeq(1,2), IndexedSeq(3,4))
a.flatten                                   // IndexedSeq[Int] = Vector(1, 2, 3, 4)

# map, flatMap
val fruits = IndexedSeq("apple", "pear")
fruits.map(_.toUpperCase)                   // Vector(APPLE, PEAR)
fruits.flatMap(_.toUpperCase)               // Vector(A, P, P, L, E, P, E, A, R)

IndexedSeq(1,2,3).reverse                   // Vector(3, 2, 1)

val nums = IndexedSeq(10, 5, 8, 1, 7)
nums.sorted                                 // Vector(1, 5, 7, 8, 10)
nums.sortWith(_ < _)                        // Vector(1, 5, 7, 8, 10)
nums.sortWith(_ > _)                        // Vector(10, 8, 7, 5, 1)

IndexedSeq(1,2,3).updated(0,10)             // Vector(10, 2, 3)
IndexedSeq(2,4).union(IndexedSeq(1,3))      // Vector(2, 4, 1, 3)

# zip
val women = IndexedSeq("Wilma", "Betty")    // Vector(Wilma, Betty)
val men = IndexedSeq("Fred", "Barney")      // Vector(Fred, Barney)
val couples = women.zip(men)                // Vector((Wilma,Fred), (Betty,Barney))

val a = IndexedSeq.range('a', 'e')          // Vector(a, b, c, d)
a.zipWithIndex                              // Vector((a,0), (b,1), (c,2), (d,3))


Informational and mathematical methods: These methods let you obtain information from a collection.

contains(e) -->	True if the sequence contains the element e
containsSlice(s) -->	True if the sequence contains the sequence s
count(p) -->	The number of elements in the sequence for which the predicate is true
endsWith(s) -->	True if the sequence ends with the sequence s
exists(p) -->	True if the predicate returns true for at least one element in the sequence
find(p) -->	The first element that matches the predicate p, returned as an Option
forall(p) -->	True if the predicate p is true for all elements in the sequence
hasDefiniteSize -->	True if the sequence has a finite size
indexOf(e) -->	The index of the first occurrence of the element e in the sequence
indexOf(e,i) -->	The index of the first occurrence of the element e in the sequence, searching only from the value of the start index i
indexOfSlice(s) -->	The index of the first occurrence of the sequence s in the sequence
indexOfSlice(s,i) -->	The index of the first occurrence of the sequence s in the sequence, searching only from the value of the start index i
indexWhere(p) -->	The index of the first element where the predicate p returns true
indexWhere(p,i) -->	The index of the first element where the predicate p returns true, searching only from the value of the start index i
isDefinedAt(i) -->	True if the sequence contains the index i
isEmpty -->	True if the sequence contains no elements
lastIndexOf(e) -->	The index of the last occurrence of the element e in the sequence
lastIndexOf(e,i) -->	The index of the last occurrence of the element e in the sequence, occurring before or at the index i
lastIndexOfSlice(s) -->	The index of the last occurrence of the sequence s in the sequence
lastIndexOfSlice(s,i) -->	The index of the last occurrence of the sequence s in the sequence, occurring before or at the index i
lastIndexWhere(p) -->	The index of the first element where the predicate p returns true
lastIndexWhere(p,i) -->	The index of the first element where the predicate p returns true, occurring before or at the index i
max -->	The largest element in the sequence
min -->	The smallest element in the sequence
nonEmpty -->	True if the sequence is not empty (i.e., if it contains 1 or more elements)
product -->	The result of multiplying the elements in the collection
segmentLength(p,i) -->	The length of the longest segment for which the predicate p is true, starting at the index i
size -->	The number of elements in the sequence
startsWith(s) -->	True if the sequence begins with the elements in the sequence s
startsWith(s,i) -->	True if the sequence has the sequence s starting at the index i
sum --> The sum of the elements in the sequence
fold(s)(o) --> "Fold" the elements of the sequence using the binary operator o, using an initial seed s (see also reduce)
foldLeft(s)(o) --> "Fold" the elements of the sequence using the binary operator o, using an initial seed s, going from left to right (see also reduceLeft)
foldRight(s)(o) --> "Fold" the elements of the sequence using the binary operator o, using an initial seed s, going from right to left (see also reduceRight)
reduce -->	"Reduce" the elements of the sequence using the binary operator o
reduceLeft -->	"Reduce" the elements of the sequence using the binary operator o, going from left to right
reduceRight -->	"Reduce" the elements of the sequence using the binary operator o, going from right to left

Examples: 
Some sample data:
val evens = IndexedSeq(2, 4, 6)               // Vector(2, 4, 6)
val odds = IndexedSeq(1, 3, 5)                // Vector(1, 3, 5)
val fbb = "foo bar baz"                       // String = foo bar baz
val firstTen = (1 to 10).toIndexedSeq         // IndexedSeq[Int] = Range 1 to 10
val fiveToFifteen = (5 to 15).toIndexedSeq    // IndexedSeq[Int] = Range 5 to 15
val empty = IndexedSeq[Int]()                 // Vector()
val letters = ('a' to 'f').toIndexedSeq       // IndexedSeq[Char] = NumericRange a to f


The examples:

evens.contains(2)                           // true
firstTen.containsSlice(IndexedSeq(3,4,5))   // true
firstTen.count(_ % 2 == 0)                  // 5
firstTen.endsWith(IndexedSeq(9,10))         // true
firstTen.exists(_ > 10)                     // false
firstTen.find(_ > 2)                        // Some(3)
firstTen.forall(_ < 20)                     // true
firstTen.hasDefiniteSize                    // true
empty.hasDefiniteSize                       // true
letters.indexOf('b')                        // 1 (zero-based)
letters.indexOf('d', 2)                     // 3
letters.indexOf('d', 3)                     // 3
letters.indexOf('d', 4)                     // -1
letters.indexOfSlice(IndexedSeq('c','d'))     // 2
letters.indexOfSlice(IndexedSeq('c','d'),2)   // 2
letters.indexOfSlice(IndexedSeq('c','d'),3)   // -1
firstTen.indexWhere(_ == 3)                 // 2
firstTen.indexWhere(_ == 3, 2)              // 2
firstTen.indexWhere(_ == 3, 5)              // -1
letters.isDefinedAt(1)                      // true
letters.isDefinedAt(20)                     // false
letters.isEmpty                             // false
empty.isEmpty                               // true

# lastIndex...
val fbb = "foo bar baz"
fbb.indexOf('a')                            // 5
fbb.lastIndexOf('a')                        // 9
fbb.lastIndexOf('a', 10)                    // 9
fbb.lastIndexOf('a', 9)                     // 9
fbb.lastIndexOf('a', 6)                     // 5
fbb.lastIndexOf('a', 5)                     // 5
fbb.lastIndexOf('a', 4)                     // -1

fbb.lastIndexOfSlice("ar")                     // 5
fbb.lastIndexOfSlice(IndexedSeq('a','r'))      // 5
fbb.lastIndexOfSlice(IndexedSeq('a','r'), 4)   // -1
fbb.lastIndexOfSlice(IndexedSeq('a','r'), 5)   // 5
fbb.lastIndexOfSlice(IndexedSeq('a','r'), 6)   // 5

fbb.lastIndexWhere(_ == 'a')                // 9
fbb.lastIndexWhere(_ == 'a', 4)             // -1
fbb.lastIndexWhere(_ == 'a', 5)             // 5
fbb.lastIndexWhere(_ == 'a', 6)             // 5
fbb.lastIndexWhere(_ == 'a', 8)             // 5
fbb.lastIndexWhere(_ == 'a', 9)             // 9

firstTen.max                                // 10
letters.max                                 // f
firstTen.min                                // 1
letters.min                                 // a
letters.nonEmpty                            // true
empty.nonEmpty                              // false
firstTen.product                            // 3628800
letters.size                                // 6

val x = IndexedSeq(1,2,9,1,1,1,1,4)
x.segmentLength(_ < 4, 0)                   // 2
x.segmentLength(_ < 4, 2)                   // 0
x.segmentLength(_ < 4, 3)                   // 4
x.segmentLength(_ < 4, 4)                   // 3

firstTen.startsWith(IndexedSeq(1,2))        // true
firstTen.startsWith(IndexedSeq(1,2), 0)     // true
firstTen.startsWith(IndexedSeq(1,2), 1)     // false
firstTen.sum                                // 55
firstTen.fold(100)(_ + _)                   // 155
firstTen.foldLeft(100)(_ + _)               // 155
firstTen.foldRight(100)(_ + _)              // 155
firstTen.reduce(_ + _)                      // 55
firstTen.reduceLeft(_ + _)                  // 55
firstTen.reduceRight(_ + _)                 // 55

firstTen.fold(100)(_ - _)                   // 45
firstTen.foldLeft(100)(_ - _)               // 45
firstTen.foldRight(100)(_ - _)              // 95
firstTen.reduce(_ - _)                      // -53
firstTen.reduceLeft(_ - _)                  // -53
firstTen.reduceRight(_ - _)                 // -5

Note: Methods like foldRight and reduceRight are not recommended with IndexedSeq because they will be very slow for large collections.

Grouping methods:These methods generally let you create multiple groups from a collection.

groupBy(f) -->	A map of collections created by the function f
grouped -->	Breaks the sequence into fixed-size iterable collections
partition(p) -->	Two collections created by the predicate p
sliding(i,s) -->	Group elements into fixed size blocks by passing a sliding window of size i and step s over them
span(p) -->	A collection of two collections; the first created by sequence.takeWhile(p), and the second created by sequence.dropWhile(p)
splitAt(i) -->	A collection of two collections by splitting the sequence at index i
unzip -->	The opposite of zip, breaks a collection into two collections by dividing each element into two pieces; such as breaking up a sequence of Tuple2 elements



Examples
val firstTen = (1 to 10).toIndexedSeq    // IndexedSeq[Int] = Range 1 to 10

firstTen.groupBy(_ > 5)                  // IndexedSeq[Int]] = Map(false -> Vector(1, 2, 3, 4, 5), true -> Vector(6, 7, 8, 9, 10))
firstTen.grouped(2)                      // IndexedSeq[Int]] = non-empty iterator
firstTen.grouped(2).toIndexedSeq         // Vector(Vector(1, 2), Vector(3, 4), Vector(5, 6), Vector(7, 8), Vector(9, 10))
firstTen.grouped(5).toIndexedSeq         // Vector(Vector(1, 2, 3, 4, 5), Vector(6, 7, 8, 9, 10))

"foo bar baz".partition(_ < 'c')         // (" ba ba", foorz)  // a Tuple2
firstTen.partition(_ > 5)                // (Vector(6, 7, 8, 9, 10),Vector(1, 2, 3, 4, 5))

firstTen.sliding(2)                      // IndexedSeq[Int]] = non-empty iterator
firstTen.sliding(2).toIndexedSeq         // Vector(Vector(1, 2), Vector(2, 3), Vector(3, 4), Vector(4, 5), Vector(5, 6), Vector(6, 7), Vector(7, 8), Vector(8, 9), Vector(9, 10))
firstTen.sliding(2,2).toIndexedSeq       // Vector(Vector(1, 2), Vector(3, 4), Vector(5, 6), Vector(7, 8), Vector(9, 10))
firstTen.sliding(2,3).toIndexedSeq       // Vector(Vector(1, 2), Vector(4, 5), Vector(7, 8), Vector(10))
firstTen.sliding(2,4).toIndexedSeq       // Vector(Vector(1, 2), Vector(5, 6), Vector(9, 10))

val x = IndexedSeq(15, 10, 5, 8, 20, 12)
x.groupBy(_ > 10)                        // Map[Boolean,IndexedSeq[Int]] = Map(false -> Vector(10, 5, 8), true -> Vector(15, 20, 12))
x.partition(_ > 10)                      // (Vector(15, 20, 12),Vector(10, 5, 8))
x.span(_ < 20)                           // (Vector(15, 10, 5, 8),Vector(20, 12))
x.splitAt(2)                             // (Vector(15, 10),Vector(5, 8, 20, 12))

