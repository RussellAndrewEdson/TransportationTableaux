/*
 * Copyright (c) 2013-2014 Russell Andrew Edson
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

// Spec for the TransportationTableau class.
// These will mainly consist of examples to exercise the class (the
// examples have been solved beforehand, and each part of the solution
// can be checked).

import org.scalatest.FlatSpec

class TransportationTableauSpec extends FlatSpec {
  
  // Code to test a worked example
  def example1 = 
    new {
      val supplies = Array[Int](100, 130, 170)
      val demands = Array[Int](150, 120, 80, 50)
      val costs = Array(Array(3,5,7,11), Array(1,4,6,3), Array(5,8,12,7))

      /* The initial tableau. */
      val initial = new TransportationTableau(supplies, demands, costs)

      /* The tableau after the North-West corner rule. */
      val step1 = new TransportationTableau(supplies, demands, costs)
      step1.northWestCornerRule()
      
      /* The tableau after the first adjustment. */
      val step2 = new TransportationTableau(supplies, demands, costs)
      step2.northWestCornerRule()
      step2.adjustAllocations(step2.cycleTraversal(step2.starPair))

      /* The tableau after the second adjustment. */
      val step3 = new TransportationTableau(supplies, demands, costs)
      step3.northWestCornerRule()
      step3.adjustAllocations(step3.cycleTraversal(step3.starPair))
      step3.adjustAllocations(step3.cycleTraversal(step3.starPair))

      /* The tableau after the third adjustment. */
      val step4 = new TransportationTableau(supplies, demands, costs)
      step4.northWestCornerRule()
      step4.adjustAllocations(step4.cycleTraversal(step4.starPair))
      step4.adjustAllocations(step4.cycleTraversal(step4.starPair))
      step4.adjustAllocations(step4.cycleTraversal(step4.starPair))

      /* The tableau after the fourth adjustment. */
      val step5 = new TransportationTableau(supplies, demands, costs)
      step5.northWestCornerRule()
      step5.adjustAllocations(step5.cycleTraversal(step5.starPair))
      step5.adjustAllocations(step5.cycleTraversal(step5.starPair))
      step5.adjustAllocations(step5.cycleTraversal(step5.starPair))
      step5.adjustAllocations(step5.cycleTraversal(step5.starPair))

    }

  "The Example 1 tableau" should 
     "be initialised with supplies (100, 130, 170)" in {
    assert(example1.initial.supplies.deep == Array(100, 130, 170).deep) 
  }

  it should "be initialised with demands (150, 120, 80, 50)" in {
    assert(example1.initial.demands.deep == Array(150, 120, 80, 50).deep)
  }

  it should "have link-flow costs ((3,5,7,11), (1,4,6,3), (5,8,12,7))" in {
    assert(example1.initial.linkFlowCosts.deep == 
        Array(Array(3,5,7,11), Array(1,4,6,3), Array(5,8,12,7)).deep)
  }
    
  it should "have all allocations initialised to 0" in {
    assert(example1.initial.allocations.flatten.forall {_ == 0})
  }

  it should "not be optimal before we've started" in {
    assert(example1.initial.isOptimal == false)
  }

  it should "not provide a basic solution before we've started" in {
    assert(example1.initial.basicSolution == List())
  }

  it should "start with a cost of 0 (no allocations made yet)" in {
    assert(example1.initial.cost == 0)
  }

  it should "have the ui dual variables initialised to 0" in {
    assert(example1.initial.ui.deep == Array(0,0,0).deep)
  }

  it should "have the vj dual variables initialised to 0" in {
    assert(example1.initial.vj.deep == Array(0,0,0,0).deep)
  }

  it should "provide the correct set of indices when asked" in {
    assert(example1.initial.indices == 
      (for (i <- 0 until 3; j <- 0 until 4) yield (i,j)) )
  }

  it should "complete the North-West corner rule without error" in {
    example1.initial.northWestCornerRule()
  }

  "After the North-West corner rule, the Example 1 tableau" should 
      "then have the allocations ((100,0,0,0), (50,80,0,0), (0,40,80,50))" in {
    assert(example1.step1.allocations.deep ==
        Array(Array(100,0,0,0), Array(50,80,0,0), Array(0,40,80,50)).deep)
  }

  it should 
      "have the basic solution ((0,0), (1,0), (1,1), (2,1), (2,2), (2,3)" in {
    assert(example1.step1.basicSolution.toSet == 
        Set((0,0), (1,0), (1,1), (2,1), (2,2), (2,3)) )
  }

  it should "have a cost of 2300" in {
    assert(example1.step1.cost == 2300)
  }

  it should "have ui = (0, -2, 2)" in {
    assert(example1.step1.ui.deep == Array(0, -2, 2).deep)
  }

  it should "have vj = (3, 6, 10, 5)" in {
    assert(example1.step1.vj.deep == Array(3, 6, 10, 5).deep)
  }

  it should "not be optimal yet" in {
    assert(example1.step1.isOptimal == false)
  }

  it should "have its star pair as (0,1)" in {
    assert(example1.step1.starPair == (0,1))
  }

  it should "have the cycle (0,1)->(0,0)->(1,0)->(1,1)" in {
    assert(example1.step1.cycleTraversal(example1.step1.starPair) ==
        List((0,1), (0,0), (1,0), (1,1)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example1.step1.adjustAllocations(
        example1.step1.cycleTraversal(example1.step1.starPair))
  }

  "After the 1st adjustment, the Example 1 tableau" should
      "then have the allocations ((20,80,0,0), (130,0,0,0), (0,40,80,50))" in {
    assert(example1.step2.allocations.deep ==
        Array(Array(20,80,0,0), Array(130,0,0,0), Array(0,40,80,50)).deep)
  }

  it should 
      "have the basic solution ((0,0), (0,1), (1,0), (2,1), (2,2), (2,3))" in {
    assert(example1.step2.basicSolution.toSet ==
        Set((0,0), (0,1), (1,0), (2,1), (2,2), (2,3)) )
  }

  it should "have a cost of 2220" in {
    assert(example1.step2.cost == 2220)
  }

  it should "have ui = (0, -2, 3)" in {
    assert(example1.step2.ui.deep == Array(0, -2, 3).deep)
  }

  it should "have vj = (3, 5, 9, 4)" in {
    assert(example1.step2.vj.deep == Array(3, 5, 9, 4).deep)
  }

  it should "not be optimal yet" in {
    assert(example1.step2.isOptimal == false)
  }

  it should "have its star pair as (0,2)" in {
    assert(example1.step2.starPair == (0,2))
  }

  it should "have the cycle (0,2)->(0,1)->(2,1)->(2,2)" in {
    assert(example1.step2.cycleTraversal(example1.step2.starPair) ==
        List((0,2), (0,1), (2,1), (2,2)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example1.step2.adjustAllocations(
        example1.step2.cycleTraversal(example1.step2.starPair))
  }

  "After the 2nd adjustment, the Example 1 tableau" should
      "then have the allocations ((20,0,80,0), (130,0,0,0), (0,120,0,50))" in {
    assert(example1.step3.allocations.deep ==
        Array(Array(20,0,80,0), Array(130,0,0,0), Array(0,120,0,50)).deep)
  }

  it should
      "have the basic solution ((0,0), (0,2), (1,0), (2,1), (2,2), (2,3))" in {
    assert(example1.step3.basicSolution.toSet ==
        Set((0,0), (0,2), (1,0), (2,1), (2,2), (2,3)) )
  }

  it should "have a cost of 2060" in {
    assert(example1.step3.cost == 2060)
  }

  it should "have ui = (0, -2, 5)" in {
    assert(example1.step3.ui.deep == Array(0, -2, 5).deep)
  }

  it should "have vj = (3, 3, 7, 2)" in {
    assert(example1.step3.vj.deep == Array(3, 3, 7, 2).deep)
  }

  it should "not be optimal yet" in {
    assert(example1.step3.isOptimal == false)
  }

  it should "have its star pair as (2,0)" in {
    assert(example1.step3.starPair == (2,0)) 
  }

  it should "have the cycle (2,0)->(2,2)->(0,2)->(0,0)" in {
    assert(example1.step3.cycleTraversal(example1.step3.starPair) ==
        List((2,0), (2,2), (0,2), (0,0)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example1.step3.adjustAllocations(
        example1.step3.cycleTraversal(example1.step3.starPair))
  }

  "After the 3rd adjustment, the Example 1 tableau" should
      "then have the allocations ((20,0,80,0), (130,0,0,0), (0,120,0,50))" in {
    assert(example1.step4.allocations.deep ==
        Array(Array(20,0,80,0), Array(130,0,0,0), Array(0,120,0,50)).deep)
  }

  it should
      "have the basic solution ((0,0), (0,2), (1,0), (2,0), (2,1), (2,3))" in {
    assert(example1.step4.basicSolution.toSet ==
        Set((0,0), (0,2), (1,0), (2,0), (2,1), (2,3)) )
  }

  it should "have a cost of 2060" in {
    assert(example1.step4.cost == 2060)
  }

  it should "have ui = (0, -2, 2)" in {
    assert(example1.step4.ui.deep == Array(0, -2, 2).deep)
  }

  it should "have vj = (3, 6, 7, 5)" in {
    assert(example1.step4.vj.deep == Array(3, 6, 7, 5).deep)
  }

  it should "not be optimal yet" in {
    assert(example1.step4.isOptimal == false)
  }

  it should "have its star pair as (0,1)" in {
    assert(example1.step4.starPair == (0,1))
  }

  it should "have the cycle (0,1)->(0,0)->(2,0)->(2,1)" in {
    assert(example1.step4.cycleTraversal(example1.step4.starPair) ==
        List((0,1), (0,0), (2,0), (2,1)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example1.step4.adjustAllocations(
        example1.step4.cycleTraversal(example1.step4.starPair))
  }

  "After the 4th adjustment, the Example 1 tableau" should
      "then have the allocations ((0,20,80,0), (130,0,0,0), (20,100,0,50))" in {
    assert(example1.step5.allocations.deep ==
        Array(Array(0,20,80,0), Array(130,0,0,0), Array(20,100,0,50)).deep)
  }

  it should
      "have the basic solution ((0,1), (0,2), (1,0), (2,0), (2,1), (2,3))" in {
    assert(example1.step5.basicSolution.toSet ==
        Set((0,1), (0,2), (1,0), (2,0), (2,1), (2,3)) )
  }

  it should "have a cost of 2040" in {
    assert(example1.step5.cost == 2040)
  }

  it should "have ui = (0, -1, 3)" in {
    assert(example1.step5.ui.deep == Array(0, -1, 3).deep)
  }

  it should "have vj = (2, 5, 7, 4)" in {
    assert(example1.step5.vj.deep == Array(2, 5, 7, 4).deep)
  }

  it should "be optimal." in {
    assert(example1.step5.isOptimal == true)
  }

  // END EXAMPLE.


  // Code to test another example
  def example2 = 
    new {
      val supplies = Array[Int](1,1)
      val demands = Array[Int](1,1)
      val costs = Array(Array(100,1), Array(1,100))

      /* The initial tableau. */
      val initial = new TransportationTableau(supplies, demands, costs)

      /* The tableau after the North-West corner rule. */
      val step1 = new TransportationTableau(supplies, demands, costs)
      step1.northWestCornerRule()

      /* The tableau after the first adjustment. */
      val step2 = new TransportationTableau(supplies, demands, costs)
      step2.northWestCornerRule()
      step2.adjustAllocations(step2.cycleTraversal(step2.starPair))
    }

  "The Example 2 tableau" should
      "be initialised with supplies (1, 1)" in {
    assert(example2.initial.supplies.deep == Array(1, 1).deep)
  }

  it should "be initialised with demands (1, 1)" in {
    assert(example2.initial.demands.deep == Array(1, 1).deep)
  }

  it should "have link-flow costs ((100,1), (1,100))" in {
    assert(example2.initial.linkFlowCosts.deep ==
        Array(Array(100,1), Array(1,100)).deep)
  }

  it should "have all allocations initialised to 0" in {
    assert(example2.initial.allocations.flatten.forall {_ == 0})
  }

  it should "not be optimal before we've started" in {
    assert(example2.initial.isOptimal == false)
  }

  it should "not provide a basic solution before we've started" in {
    assert(example2.initial.basicSolution == List())
  }

  it should "start with a cost of 0 (no allocations made yet)" in {
    assert(example2.initial.cost == 0)
  }

  it should "have the ui dual variables initialised to 0" in {
    assert(example2.initial.ui.deep == Array(0,0).deep)
  }

  it should "have the vj dual variables initialised to 0" in {
    assert(example2.initial.vj.deep == Array(0,0).deep)
  }

  it should "provide the correct set of indices when asked" in {
    assert(example2.initial.indices ==
        (for (i <- 0 until 2; j <- 0 until 2) yield (i,j)) )
  }

  it should "complete the North-West corner rule without error" in {
    example2.initial.northWestCornerRule()
  }

  "After the North-West corner rule, the Example 2 tableau" should
      "then have the allocations ((1,0), (0,1))" in {
    assert(example2.step1.allocations.deep ==
        Array(Array(1,0), Array(0,1)).deep)
  }

  it should "have the basic solution (0,0), (0,1), (1,1)" in {
    assert(example2.step1.basicSolution.toSet ==
        Set((0,0), (0,1), (1,1)) )
  }

  it should "have a cost of 200" in {
    assert(example2.step1.cost == 200)
  }

  it should "have ui = (0, 99)" in {
    assert(example2.step1.ui.deep == Array(0, 99).deep)
  }

  it should "have vj = (100, 1)" in {
    assert(example2.step1.vj.deep == Array(100, 1).deep)
  }

  it should "not be optimal yet" in {
    assert(example2.step1.isOptimal == false)
  }

  it should "have its star pair as (1,0)" in {
    assert(example2.step1.starPair == (1,0))
  }

  it should "have the cycle (1,0)->(1,1)->(0,1)->(0,0)" in {
    assert(example2.step1.cycleTraversal(example2.step1.starPair) ==
        List((1,0), (1,1), (0,1), (0,0)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example2.step1.adjustAllocations(
        example2.step1.cycleTraversal(example2.step1.starPair))
  }

  "After the 1st adjustment, the Example 2 tableau" should
      "then have the allocations ((0,1), (1,0))" in {
    assert(example2.step2.allocations.deep == 
        Array(Array(0,1), Array(1,0)).deep)
  }

  it should "have the basic solution ((0,0), (0,1), (1,0))" in {
    assert(example2.step2.basicSolution.toSet ==
        Set((0,0), (0,1), (1,0)) )
  }

  it should "have a cost of 2" in {
    assert(example2.step2.cost == 2)
  }

  it should "have ui = (0, -99)" in {
    assert(example2.step2.ui.deep == Array(0, -99).deep)
  }

  it should "have vj = (100, 1)" in {
    assert(example2.step2.vj.deep == Array(100, 1).deep)
  }

  it should "be optimal" in {
    assert(example2.step2.isOptimal == true)
  }
  
  // END EXAMPLE


  // Code to test another example
  def example3 = 
    new {
      val supplies = Array[Int](1,1)
      val demands = Array[Int](2)
      val costs = Array(Array(0), Array(1))

      /* The initial tableau. */
      val initial = new TransportationTableau(supplies, demands, costs)

      /* The tableau after the North-West corner rule. */
      val step1 = new TransportationTableau(supplies, demands, costs)
      step1.northWestCornerRule()
    }

  "The Example 3 tableau" should
      "be initialised with supplies (1, 1)" in {
    assert(example3.initial.supplies.deep == Array(1, 1).deep)
  }

  it should "be initialised with demands (2)" in {
    assert(example3.initial.demands.deep == Array(2).deep)
  }

  it should "have link-flow costs ((0), (1))" in {
    assert(example3.initial.linkFlowCosts.deep == 
        Array(Array(0), Array(1)).deep)
  }

  it should "have all allocations initialised to 0" in {
    assert(example3.initial.allocations.flatten.forall {_ == 0})
  }

  it should "not be optimal before we've started" in {
    assert(example3.initial.isOptimal == false)
  }

  it should "not provide a basic solution before we've started" in {
    assert(example3.initial.basicSolution == List())
  }

  it should "start with a cost of 0 (no allocations made yet)" in {
    assert(example3.initial.cost == 0)
  }

  it should "have the ui dual variables initialised to 0" in {
    assert(example3.initial.ui.deep == Array(0,0).deep)
  }

  it should "have the vj dual variables initialised to 0" in {
    assert(example3.initial.vj.deep == Array(0).deep)
  }

  it should "provide the correct set of indices when asked" in {
    assert(example3.initial.indices == 
        (for (i <- 0 until 2; j <- 0 until 1) yield (i,j)) )
  }

  it should "complete the North-West corner rule without error" in {
    example3.initial.northWestCornerRule()
  }

  "After the North-West corner rule, the Example 3 tableau" should
      "then have the allocations ((1),(1))" in {
    assert(example3.step1.allocations.deep ==
        Array(Array(1), Array(1)).deep)
  }

  it should "have the basic solution (0,0), (1,0)" in {
    assert(example3.step1.basicSolution.toSet ==
        Set((0,0), (1,0)) )
  }

  it should "have a cost of 1" in {
    assert(example3.step1.cost == 1)
  }

  it should "have ui = (0, 1)" in {
    assert(example3.step1.ui.deep == Array(0, 1).deep)
  }

  it should "have vj = (0)" in {
    assert(example3.step1.vj.deep == Array(0).deep)
  }

  it should "be optimal" in {
    assert(example3.step1.isOptimal == true)
  }

  // END EXAMPLE

  // Example 4
  def example4 =
    new {
      val supplies = Array[Int](1)
      val demands = Array[Int](1)
      val costs = Array(Array(1))

      /* The initial tableau. */
      val initial = new TransportationTableau(supplies, demands, costs)

      /* The tableau after the North-West corner rule. */
      val step1 = new TransportationTableau(supplies, demands, costs)
      step1.northWestCornerRule()
    }

  "The Example 4 Tableau" should "be initialised with supplies (1)" in {
    assert(example4.initial.supplies.deep == Array(1).deep)
  }

  it should "be initialised with demands (1)" in {
    assert(example4.initial.demands.deep == Array(1).deep)
  }

  it should "have link-flow costs ((1))" in {
    assert(example4.initial.linkFlowCosts.deep == Array(Array(1)).deep)
  }

  it should "have all allocations initialised to 0" in {
    assert(example4.initial.allocations.flatten.forall {_ == 0})
  }

  it should "not be optimal before we've started" in {
    assert(example4.initial.isOptimal == false)
  }

  it should "not provide a basic solution before we've started" in {
    assert(example4.initial.basicSolution == List())
  }

  it should "start with a cost of 0 (no allocations made yet)" in {
    assert(example4.initial.cost == 0)
  }

  it should "have the ui dual variables initialised to 0" in {
    assert(example4.initial.ui.deep == Array(0).deep)
  }

  it should "have the vj dual variables initialised to 0" in {
    assert(example4.initial.vj.deep == Array(0).deep)
  }

  it should "provide the correct set of indices when asked" in {
    assert(example4.initial.indices ==
        (for (i <- 0 until 1; j <- 0 until 1) yield (i,j)) )
  }

  it should "complete the North-West corner rule without error" in {
    example4.initial.northWestCornerRule()
  }

  "After the North-West corner rule, the Example 4 tableau" should
      "then have the allocations ((1))" in {
    assert(example4.step1.allocations.deep == Array(Array(1)).deep)
  }

  it should "have the basic solution (0,0)" in {
    assert(example4.step1.basicSolution.toSet == Set((0,0)) )
  }

  it should "have a cost of 1" in {
    assert(example4.step1.cost == 1)
  }

  it should "have ui = (0)" in {
    assert(example4.step1.ui.deep == Array(0).deep)
  }

  it should "have vj = (1)" in {
    assert(example4.step1.vj.deep == Array(1).deep)
  }

  it should "be optimal" in {
    assert(example4.step1.isOptimal == true)
  }


  // END EXAMPLE

  // Example 5
  def example5 =
    new {
      val supplies = Array[Int](9, 5, 2)
      val demands = Array[Int](8, 4, 4)
      val costs = Array(Array(3, 2, 3), Array(3, 5, 1), Array(4, 6, 3))

      /* The initial tableau. */
      val initial = new TransportationTableau(supplies, demands, costs)

      /* The tableau after the North-West corner rule. */
      val step1 = new TransportationTableau(supplies, demands, costs)
      step1.northWestCornerRule()

      /* The tableau after the 1st adjustment. */
      val step2 = new TransportationTableau(supplies, demands, costs)
      step2.northWestCornerRule()
      step2.adjustAllocations(step2.cycleTraversal(step2.starPair))

      /* The tableau after the 2nd adjustment. */
      val step3 = new TransportationTableau(supplies, demands, costs)
      step3.northWestCornerRule()
      step3.adjustAllocations(step3.cycleTraversal(step3.starPair))
      step3.adjustAllocations(step3.cycleTraversal(step3.starPair))
    }

  "The Example 5 tableau" should
      "be initialised with supplies (9, 5, 2)" in {
    assert(example5.initial.supplies.deep == Array(9, 5, 2).deep)
  }

  it should "be initialised with demands (8, 4, 4)" in {
    assert(example5.initial.demands.deep == Array(8, 4, 4).deep)
  }

  it should "have link-flow costs ((3,2,3), (3,5,1), (4,6,3))" in {
    assert(example5.initial.linkFlowCosts.deep ==
        Array(Array(3,2,3), Array(3,5,1), Array(4,6,3)).deep)
  }

  it should "have all allocations initialised to 0" in {
    assert(example5.initial.allocations.flatten.forall {_ == 0})
  }

  it should "not be optimal before we've started" in {
    assert(example5.initial.isOptimal == false)
  }

  it should "not provide a basic solution before we've started" in {
    assert(example5.initial.basicSolution == List())
  }

  it should "start with a cost of 0 (no allocations made yet)" in {
    assert(example5.initial.cost == 0)
  }

  it should "have the ui dual variables initialised to 0" in {
    assert(example5.initial.ui.deep == Array(0,0,0).deep)
  }

  it should "have the vj dual variables initialised to 0" in {
    assert(example5.initial.vj.deep == Array(0,0,0).deep)
  }

  it should "provide the correct set of indices when asked" in {
    assert(example5.initial.indices == 
        (for (i <- 0 until 3; j <- 0 until 3) yield (i,j)) ) 
  }

  it should "complete the North-West corner rule without error" in {
    example5.initial.northWestCornerRule()
  }

  "After the North-West corner rule, the Example 5 tableau" should
      "then have the allocations ((8,1,0), (0,3,2), (0,0,2))" in {
    assert(example5.step1.allocations.deep ==
        Array(Array(8,1,0), Array(0,3,2), Array(0,0,2)).deep)
  }

  it should "have the basic solution (0,0), (0,1), (1,1), (1,2), (2,2)" in {
    assert(example5.step1.basicSolution.toSet ==
        Set((0,0), (0,1), (1,1), (1,2), (2,2)) )
  }

  it should "have a cost of 49" in {
    assert(example5.step1.cost == 49)
  }

  it should "have ui = (0, 3, 5)" in {
    assert(example5.step1.ui.deep == Array(0, 3, 5).deep)
  }

  it should "have vj = (3, 2, -2)" in {
    assert(example5.step1.vj.deep == Array(3, 2, -2).deep)
  }

  it should "not be optimal yet" in {
    assert(example5.step1.isOptimal == false)
  }

  it should "have its star pair as (1,0)" in {
    assert(example5.step1.starPair == (1,0))
  }

  it should "have the cycle (1,0)->(1,1)->(0,1)->(0,0)" in {
    assert(example5.step1.cycleTraversal(example5.step1.starPair) ==
        List((1,0), (1,1), (0,1), (0,0)) )
  }

  it should "adjust allocations along the cycle without error" in {
    example5.step1.adjustAllocations(
        example5.step1.cycleTraversal(example5.step1.starPair))
  }

  "After the 1st adjustment, the Example 5 tableau" should
      "then have the allocations ((5,4,0), (3,0,2), (0,0,2))" in {
    assert(example5.step2.allocations.deep ==
        Array(Array(5,4,0), Array(3,0,2), Array(0,0,2)).deep)
  }

  it should "have the basic solution (0,0), (0,1), (1,0), (1,2), (2,2)" in {
    assert(example5.step2.basicSolution.toSet ==
        Set((0,0), (0,1), (1,0), (1,2), (2,2)) )
  }

  it should "have a cost of 40" in {
    assert(example5.step2.cost == 40)
  }

  it should "have ui = (0, 0, 2)" in {
    assert(example5.step2.ui.deep == Array(0, 0, 2).deep)
  }

  it should "have vj = (3, 2, 1)" in {
    assert(example5.step2.vj.deep == Array(3, 2, 1).deep)
  }

  it should "not be optimal yet" in {
    assert(example5.step2.isOptimal == false)
  }

  it should "have its star pair as (2,0)" in {
    assert(example5.step2.starPair == (2,0))
  }

  it should "have the cycle (2,0)->(2,2)->(1,2)->(1,0)" in {
    assert(example5.step2.cycleTraversal(example5.step2.starPair) ==
        List((2,0), (2,2), (1,2), (1,0)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example5.step2.adjustAllocations(
        example5.step2.cycleTraversal(example5.step2.starPair))
  }

  "After the 2nd adjustment, the Example 5 tableau" should
      "then have the allocations ((5,4,0), (1,0,4), (2,0,0))" in {
    assert(example5.step3.allocations.deep ==
        Array(Array(5,4,0), Array(1,0,4), Array(2,0,0)).deep)
  }

  it should "have the basic solution (0,0), (0,1), (1,0), (1,2), (2,0)" in {
    assert(example5.step3.basicSolution.toSet ==
        Set((0,0), (0,1), (1,0), (1,2), (2,0)) )
  }

  it should "have a cost of 38" in {
    assert(example5.step3.cost == 38)
  }

  it should "have ui = (0, 0, 1)" in {
    assert(example5.step3.ui.deep == Array(0, 0, 1).deep)
  }

  it should "have vj = (3, 2, 1)" in {
    assert(example5.step3.vj.deep == Array(3, 2, 1).deep)
  }

  it should "be optimal" in {
    assert(example5.step3.isOptimal == true)
  }

  // END EXAMPLE

  // Example 6
  def example6 = 
    new {
      val supplies = Array[Int](250, 130, 235)
      val demands = Array[Int](75, 240, 230, 70)
      val costs = Array(
          Array(15, 20, 16, 21), Array(25, 13, 5, 11), Array(15, 15, 7, 17) )

      /* The initial tableau. */
      val initial = new TransportationTableau(supplies, demands, costs)

      /* The tableau after the North-West corner rule. */
      val step1 = new TransportationTableau(supplies, demands, costs)
      step1.northWestCornerRule()

      /* The tableau after the 1st adjustment. */
      val step2 = new TransportationTableau(supplies, demands, costs)
      step2.northWestCornerRule()
      step2.adjustAllocations(step2.cycleTraversal(step2.starPair))

      /* The tableau after the 2nd adjustment. */
      val step3 = new TransportationTableau(supplies, demands, costs)
      step3.northWestCornerRule()
      step3.adjustAllocations(step3.cycleTraversal(step3.starPair))
      step3.adjustAllocations(step3.cycleTraversal(step3.starPair))

      /* The tableay after the 3rd adjustment. */
      val step4 = new TransportationTableau(supplies, demands, costs)
      step4.northWestCornerRule()
      step4.adjustAllocations(step4.cycleTraversal(step4.starPair))
      step4.adjustAllocations(step4.cycleTraversal(step4.starPair))
      step4.adjustAllocations(step4.cycleTraversal(step4.starPair))
    }

  "The Example 6 tableau" should
      "be initialised with supplies (250, 130, 235)" in {
    assert(example6.initial.supplies.deep == Array(250, 130, 235).deep)
  }

  it should "be initialised with demands (75, 240, 230, 70)" in {
    assert(example6.initial.demands.deep == Array(75, 240, 230, 70).deep)
  }

  it should 
      "have link-flow costs ((15,20,16,21), (25,13,5,11), (15,15,7,17))" in {
    assert(example6.initial.linkFlowCosts.deep == 
        Array(Array(15,20,16,21), Array(25,13,5,11), Array(15,15,7,17)).deep)
  }

  it should "have all allocations initialised to 0" in {
    assert(example6.initial.allocations.flatten.forall {_ == 0})
  }

  it should "not be optimal before we've started" in {
    assert(example6.initial.isOptimal == false)
  }

  it should "not provide a basic solution before we've started" in {
    assert(example6.initial.basicSolution == List())
  }

  it should "start with a cost of 0 (no allocations made yet)" in {
    assert(example6.initial.cost == 0)
  }

  it should "have the ui dual variables initialised to 0" in {
    assert(example6.initial.ui.deep == Array(0,0,0).deep)
  }

  it should "have the vj dual variables initialised to 0" in {
    assert(example6.initial.vj.deep == Array(0,0,0,0).deep)
  }

  it should "provide the correct set of indices when asked" in {
    assert(example6.initial.indices ==
        (for (i <- 0 until 3; j <- 0 until 4) yield (i,j)) )
  }

  it should "complete the North-West corner rule without error" in {
    example6.initial.northWestCornerRule()
  }

  "After the North-West corner rule, the Example 6 tableau" should
      "then have the allocations ((75,175,0,0),(0,65,65,0),(0,0,165,70))" in {
    assert(example6.step1.allocations.deep ==
        Array(Array(75,175,0,0), Array(0,65,65,0), Array(0,0,165,70)).deep)
  }

  it should "have the basic solution (0,0),(0,1),(1,1),(1,2),(2,2),(2,3)" in {
    assert(example6.step1.basicSolution.toSet ==
        Set((0,0), (0,1), (1,1), (1,2), (2,2), (2,3)) )
  }

  it should "have a cost of 8140" in {
    assert(example6.step1.cost == 8140)
  }

  it should "have ui = (0, -7, -5)" in {
    assert(example6.step1.ui.deep == Array(0, -7, -5).deep)
  }

  it should "have vj = (15, 20, 12, 22)" in {
    assert(example6.step1.vj.deep == Array(15, 20, 12, 22).deep)
  }

  it should "not be optimal yet" in {
    assert(example6.step1.isOptimal == false)
  }

  it should "have its star pair as (0,3)" in {
    assert(example6.step1.starPair == (0,3))
  }

  it should "have the cycle (0,3)->(0,1)->(1,1)->(1,2)->(2,2)->(2,3)" in {
    assert(example6.step1.cycleTraversal(example6.step1.starPair) ==
        List((0,3), (0,1), (1,1), (1,2), (2,2), (2,3)) )
  }

  it should "adjust allocations along the cycle without error" in {
    example6.step1.adjustAllocations(
        example6.step1.cycleTraversal(example6.step1.starPair))
  }

  "After the 1st adjustment, the Example 6 tableau" should
      "then have the allocations ((75,110,0,65),(0,130,0,0),(0,0,230,5))" in {
    assert(example6.step2.allocations.deep ==
        Array(Array(75,110,0,65), Array(0,130,0,0), Array(0,0,230,5)).deep)
  }

  it should "have the basic solution (0,0),(0,1),(0,3),(1,1),(2,2),(2,3)" in {
    assert(example6.step2.basicSolution.toSet ==
        Set((0,0), (0,1), (0,3), (1,1), (2,2), (2,3)) )
  }

  it should "have a cost of 8075" in {
    assert(example6.step2.cost == 8075)
  }

  it should "have ui = (0, -7, -4)" in {
    assert(example6.step2.ui.deep == Array(0, -7, -4).deep)
  }

  it should "have vj = (15, 20, 11, 21)" in {
    assert(example6.step2.vj.deep == Array(15, 20, 11, 21).deep)
  }

  it should "not be optimal yet" in {
    assert(example6.step2.isOptimal == false)
  }

  it should "have its star pair as (1,3)" in {
    assert(example6.step2.starPair == (1,3))
  }

  it should "have the cycle (1,3)->(1,1)->(0,1)->(0,3)" in {
    assert(example6.step2.cycleTraversal(example6.step2.starPair) ==
        List((1,3), (1,1), (0,1), (0,3)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example6.step2.adjustAllocations(
        example6.step2.cycleTraversal(example6.step2.starPair))
  }

  "After the 2nd adjustment, the Example 6 tableau" should
      "then have the allocations ((75,175,0,0),(0,65,0,65),(0,0,230,5))" in {
    assert(example6.step3.allocations.deep ==
        Array(Array(75,175,0,0), Array(0,65,0,65), Array(0,0,230,5)).deep)
  }

  it should "have the basic solution (0,0),(0,1),(1,1),(1,3),(2,2),(2,3)" in {
    assert(example6.step3.basicSolution.toSet ==
        Set((0,0), (0,1), (1,1), (1,3), (2,2), (2,3)) )
  }

  it should "have a cost of 7880" in {
    assert(example6.step3.cost == 7880)
  }

  it should "have ui = (0, -7, -1)" in {
    assert(example6.step3.ui.deep == Array(0, -7, -1).deep)
  }

  it should "have vj = (15, 20, 8, 18)" in {
    assert(example6.step3.vj.deep == Array(15, 20, 8, 18).deep)
  }

  it should "not be optimal yet" in {
    assert(example6.step3.isOptimal == false)
  }

  it should "have its star pair as (2,1)" in {
    assert(example6.step3.starPair == (2,1))
  }

  it should "have the cycle (2,1)->(2,3)->(1,3)->(1,1)" in {
    assert(example6.step3.cycleTraversal(example6.step3.starPair) == 
        List((2,1), (2,3), (1,3), (1,1)) )
  }

  it should "adjust the allocations along the cycle without error" in {
    example6.step3.adjustAllocations(
        example6.step3.cycleTraversal(example6.step3.starPair))
  }

  "After the 3rd adjustment, the Example 6 tableau" should
      "then have the allocations ((75,175,0,0),(0,60,0,70),(0,5,230,0))" in {
    assert(example6.step4.allocations.deep ==
        Array(Array(75,175,0,0), Array(0,60,0,70), Array(0,5,230,0)).deep)
  }

  it should "have the basic solution (0,0),(0,1),(1,1),(1,3),(2,1),(2,2)" in {
    assert(example6.step4.basicSolution.toSet ==
        Set((0,0), (0,1), (1,1), (1,3), (2,1), (2,2)) )
  }

  it should "have a cost of 7860" in {
    assert(example6.step4.cost == 7860)
  }

  it should "have ui = (0, -7, -5)" in {
    assert(example6.step4.ui.deep == Array(0, -7, -5).deep)
  }

  it should "have vj = (15, 20, 12, 18)" in {
    assert(example6.step4.vj.deep == Array(15, 20, 12, 18).deep)
  }

  it should "be optimal" in {
    assert(example6.step4.isOptimal == true)
  }

}

