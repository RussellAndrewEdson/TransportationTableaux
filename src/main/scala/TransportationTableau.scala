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
 
/** A Transportation Tableau used to solve a Balanced Transportation Problem
  * from Operations Research in a step-by-step manner for demonstration
  * purposes.
  *
  * Class usage example:
  * {{{
  *   // Set up a Transportation Problem to be solved:
  *   val sup = Array[Int](9, 5, 2)
  *   val dem = Array[Int](8, 4, 4)
  *   val linkFlow = Array(Array(3,2,3), Array(3,5,1), Array(4,6,3))
  *   val tab = new TransportationTableau(sup, dem, linkFlow)
  *
  *   // We always perform the North-West corner rule to begin with.
  *   tab.northWestCornerRule()
  *
  *   // We can check the initial allocations and the associated cost:
  *   tab.allocations               // ((8,1,0), (0,3,2), (0,0,2))
  *   tab.cost()                    // 49
  *
  *   // We can get the basic solution at any time.
  *   tab.basicSolution             // ((2,2), (1,2), (1,1), (0,1), (0,0))
  *
  *   // To improve the tableau, we first find the "star pair":
  *   val star = tab.starPair()     // (1,0)
  *
  *   // The star pair is then used to form a cycle in the tableau:
  *   val cycle = tab.cycleTraversal(star)    
  *                                 // ((1,0), (1,1), (0,1), (0,0))
  *
  *   // The cycle is then used to adjust the allocations:
  *   tab.adjustAllocations(cycle)
  *   tab.allocations               // ((5,4,0), (3,0,2), (0,0,2))
  *
  *   // The cost will be smaller (or equal) for this new allocation.
  *   tab.cost()                    // 40
  *
  *   // We can also view the ui and vj dual variables at any time:
  *   tab.ui                        // (0,0,2)
  *   tab.vj                        // (3,2,1)
  *
  *   // Finally, we can check if we've reached the optimal allocation.
  *   tab.isOptimal()               // false
  *
  *   // We can loop until the tableau has been solved:
  *   while (!tab.isOptimal()) {
  *     tab.adjustAllocations( tab.cycleTraversal(tab.starPair) )
  *     // We can print intermediate tableau/star pairs/cycles as needed.
  *   }
  *
  *   // When the loop finishes, we will have the optimal allocation of
  *   // supply to demand, and the minimum possible cost of transportation.
  *   tab.allocations               // ((5,4,0), (1,0,4), (2,0,0)) 
  *   tab.cost()                    // 38
  * }}}
  *
  * @constructor Create a new Transportation Tableau with the given supplies,
  *              demands, and link-flow costs.
  * @param supplies The supplies for the Transportation Problem.
  * @param demands The demands for the Transportation Problem.
  * @param linkFlowCosts The matrix of link-flow costs for supply to demand.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 1.0
  */
class TransportationTableau(
    val supplies: Array[Int],
    val demands: Array[Int],
    val linkFlowCosts: Array[Array[Int]] ) {
  
  /** The current supply-to-demand allocations matrix for the tableau. */
  val allocations = Array.ofDim[Int](supplies.length, demands.length)

  /** The ui variables for the dual problem. These are used to determine
    * dual-feasibility, which will tell us when the tableau is optimal.
    */
  val ui = Array.fill[Int](supplies.length)(0)

  /** The vj variables for the dual problem. These are used to determine
    * dual-feasibility, which will tell us when the tableau is optimal.
    */
  val vj = Array.fill[Int](demands.length)(0)

  /** The list of basic pairs for the current tableau.
    *
    * The basic pairs are generally the indices for the basic variables: 
    * the non-trivial allocations in the tableau (though sometimes we 
    * will have indices with zero allocations here -- this is because
    * the algorithm keeps the number of basic variables constant once the
    * initial allocation has been made with the North-West Corner Rule.)
    */
  private var basicPairs = List[Tuple2[Int, Int]]()

  /** The indices for the allocation and link-flow cost matrices,
    * provided to allow for easy iteration over all rows and columns.
    */
  val indices = for (i <- supplies.indices; j <- demands.indices) yield (i,j)

  /** Returns the list of index pairs that are considered adjcent to the
    * given pair in the tableau (ie. they are the closest pairs in each
    * direction left, right, up or down.)
    *
    * @param pair The pair to find the adjacent pairs for.
    * @param candidates The pairs from which to check for adjacency.
    * @return The list of pairs that are adjacent to the given pair.
    */
  private def adjacentPairs(
      pair: Tuple2[Int, Int],
      candidates: List[Tuple2[Int, Int]] ): List[Tuple2[Int, Int]] = {
    
    /* First, we isolate all of the pairs to the left, right, up and down
     * from the given pair respectively.
     */
    val left  = candidates.filter { p => p._2 == pair._2 && p._1 < pair._1 }
    val right = candidates.filter { p => p._2 == pair._2 && p._1 > pair._1 }
    val up    = candidates.filter { p => p._1 == pair._1 && p._2 > pair._2 }
    val down  = candidates.filter { p => p._1 == pair._1 && p._2 < pair._2 }

    /* Now we pick the closest pair from those lists. */
    var adjacent = List[Tuple2[Int, Int]]()
    if (left.nonEmpty)  adjacent = left.maxBy { _._1 } :: adjacent
    if (right.nonEmpty) adjacent = right.minBy { _._1 } :: adjacent
    if (up.nonEmpty)    adjacent = up.minBy { _._2 } :: adjacent
    if (down.nonEmpty)  adjacent = down.maxBy { _._2 } :: adjacent

    adjacent
  }

  /** Adjusts the supply-to-demand allocations according to the given 
    * tableau cycle, and updates the set of basic pairs to reflect
    * the change.
    *
    * @param cycle The tableau cycle to use to adjust the allocations.
    *              This cycle can be found using the cycleTraversal() method.
    */
  def adjustAllocations(cycle: List[Tuple2[Int, Int]]) {
    /* We label the cycle pairs +,- alternately. */
    val plusPairs = cycle.zipWithIndex filter { _._2 % 2 == 0 } map { _._1 }
    val minusPairs = cycle.zipWithIndex filter { _._2 % 2 != 0 } map { _._1 }

    /* Next, we find the minus-labelled pair with the smallest allocation. */
    val minimumPair = minusPairs.minBy[Int] { p => allocations(p._1)(p._2) }
    val minimumAllocation = allocations(minimumPair._1)(minimumPair._2)

    /* This smallest allocation is added to the plus pairs, and subtracted
     * from the minus pairs for the allocation adjustment. This keeps the
     * total allocation in each row/column constant as required.
     */
    plusPairs.foreach { p => allocations(p._1)(p._2) += minimumAllocation }
    minusPairs.foreach { p => allocations(p._1)(p._2) -= minimumAllocation }

    /* The basic pairs are updated by removing the minimum pair,
     * which now has no allocation, and adding the head of the cycle
     * (the star pair), which we consider to have had an allocation (even
     * though the allocation may have been +0.)
     */
    basicPairs = basicPairs diff(List(minimumPair))
    basicPairs = cycle.head :: basicPairs

    /* Finally, we solve for the ui, vj dual variables. */
    solveUiVj()
  }

  /** Returns the current basic solution for the Transportation Problem.
    *
    * @return The list of basic pairs for the current allocation.
    */
  def basicSolution: List[Tuple2[Int, Int]] = basicPairs

  /** Returns the current cost for the Transportation Tableau.
    *
    * The cost is determined by simply summing up the link-flow costs for
    * each allocation that we've made.
    *
    * @return The current cost for the tableau.
    */
  def cost(): Int =
    indices.foldLeft(0) {
      (sum, p) => sum + allocations(p._1)(p._2) * linkFlowCosts(p._1)(p._2)
    }

  /** Returns the minimal cycle containing the given "star pair" and the
    * tableau basic pairs, in traversal order starting from the star pair. 
    * If no such cycle exists, this method returns an empty tuple list 
    * reflecting this.
    *
    * @param starPair The star pair to use for the start of the cycle.
    * @return The cycle if it exists, and an empty List otherwise.
    */
  def cycleTraversal(starPair: Tuple2[Int, Int]): List[Tuple2[Int, Int]] = {
    /* We first isolate the cycle in the star pair and basic pairs, and then 
     * "prune" it to the smallest possible cycle with no redundant pairs 
     * included. We do this by taking advantage of the fact that our 
     * isolateCycle() method is defined to return an empty list if no 
     * cycle exists.
     */
    var cycle = isolateCycle(starPair :: basicPairs)
    if (cycle.nonEmpty) {
      /* We make a copy of the cycle here so we can safely remove pairs from
       * it during the loop.
       */
      var minimalCycle = cycle
      for (pair <- cycle) {
        val cycleWithoutPair = isolateCycle( (cycle diff List(pair)) )
        if (cycleWithoutPair.nonEmpty)
          /* In this case we can remove the pair from the cycle
           * without affecting the cycle structure.
           */
          minimalCycle = minimalCycle diff List(pair)
      }

      /* Finally, we sort the cycle in traversal order, starting from the
       * star pair.
       */
      var traversalOrder = starPair :: Nil
      minimalCycle = minimalCycle diff List(starPair)

      while (minimalCycle.nonEmpty) {
        /* We consider the current head of the traversal. */
        val currentPair = traversalOrder.head
        val adjacent = adjacentPairs(currentPair, minimalCycle)

        /* Here we arbitrarily pick the direction to traverse along the
         * cycle in. We always go with whatever the first pair in the
         * adjacent list is, so notice that we favour going left/up before
         * right/down. 
         * 
         * Also note that we only have to make this choice for the first pair;
         * after that, there should only ever be one pair in the adjacent
         * list to pick from.
         */
        traversalOrder = (adjacent.head) :: traversalOrder
        minimalCycle = minimalCycle diff List(adjacent.head) 
      }

      /* Since we prepended the next nodes (to be efficient), we reverse the
       * traversal list to get the proper order.
       */
      cycle = traversalOrder.reverse
    }

    cycle
  }

  /** Returns true if the optimal tableau has been reached for the problem.
    *
    * The optimum will be reached when we have dual-feasibility, ie. when 
    * the sum of the solved ui and vj values is less than the cost value.
    * (Note that we always return false if no allocations have been made yet.)
    *
    * @return True if the tableau is optimal, false if not.
    */
  def isOptimal(): Boolean = 
    if (allocations.flatten forall { _ == 0 }) 
      false
    else
      indices.forall { p => ui(p._1) + vj(p._2) <= linkFlowCosts(p._1)(p._2) }

  /** Returns the cycle in a given set of pairs, provided exactly one cycle
    * exists. If no cycle exists, this method returns an empty list.
    *
    * (If multiple cycles exist [which shouldn't happen in the program], this
    * method will have only removed those pairs that don't exist on any of the
    * cycles.)
    *
    * @param pairs The pairs to isolate the cycle from.
    * @return The cycle if it exists, and an empty list otherwise.
    */
  private def isolateCycle(
      pairs: List[Tuple2[Int, Int]]): List[Tuple2[Int, Int]] = {
    
    /* We repeatedly remove any pairs with less than 2 adjacent pairs 
     * (this means that those pairs are "leaves", and can be safely removed
     * without losing the cycle.)
     */
    val leaves = pairs.filter { p => adjacentPairs(p, pairs).length < 2 }

    /* If we didn't end up filtering out any non-cycle pairs, we're done.
     * Otherwise, we recursively filter the new set of pairs.
     */
    // TODO: Check whether this is actually tail-recursive...
    if (leaves.isEmpty) pairs else isolateCycle( (pairs diff(leaves)) )
  }

  /** Applies the North-West corner rule to the tableau to find an initial
    * basic solution to the transportation problem.
    *
    * It does this by starting in the North-West corner and moving along the
    * grid, allocating as much supply to meet demand as it can in each cell.
    * When the process is finished, we will have an initial basic solution to
    * the Transportation Problem, though it may not be the optimal solution.
    */
  def northWestCornerRule() {
    /* We start in the North-West corner of the tableau. */
    var (i,j) = (0,0)
    var canSupply = supplies(i)
    var canDemand = demands(j)

    /* We move along the grid, allocating the smaller of the supply and demand
     * each time. When the supply is exhausted, we move to the next row of the
     * grid; similarly, we move to the next column if the demand has been met.
     */
    while (i < allocations.length && j < allocations(i).length) {
      basicPairs = (i,j) :: basicPairs
      if (canSupply < canDemand) {
        /* We don't have enough supply to meet demand, so we allocate what
         * we can and move to the next supplier.
         */
        allocations(i)(j) = canSupply
        canDemand -= canSupply
        i += 1
        if (i < supplies.length)
          canSupply = supplies(i)
      }
      else {
        /* We can fully meet demand here, so we allocate enough supply to
         * meet this demand, and move on along to the next demand.
         */
        allocations(i)(j) = canDemand
        canSupply -= canDemand
        j += 1
        if (j < demands.length)
          canDemand = demands(j)
      }
    }

    /* Finally, we solve for the ui and vj values. */
    solveUiVj()
  }

  /** Solves the ui and vj values for the dual problem according to the
    * current tableau allocations.
    *
    * This is a private helper method that is called whenever the
    * allocations have been changed (this saves the user the trouble of
    * having to call it manually themselves.)
    */
  private def solveUiVj() {
    /* We keep track of which ui and vj values have been changed. */
    val uiChanged = Array.fill[Boolean](ui.length)(false)
    val vjChanged = Array.fill[Boolean](vj.length)(false)
    val filledUiIndices = scala.collection.mutable.Queue[Int]()
    val filledVjIndices = scala.collection.mutable.Queue[Int]()

    /* We define a helper procedure to set a ui value (which includes
     * updating the above trackers accordingly.)
     */
    def setUi(index: Int, value: Int) {
      ui(index) = value
      filledUiIndices += index
      uiChanged(index) = true
    }

    /* We define a similar procedure for the vj values, which also
     * updates its own trackers as necessary.
     */
    def setVj(index: Int, value: Int) {
      vj(index) = value
      filledVjIndices += index
      vjChanged(index) = true
    }

    /* By convention, we set ui(0) = 0, and use this to determine the
     * rest of the ui and vj values.
     */
    setUi(0,0)

    /* We move around the grid, filling in the rest of the ui and vj values
     * based on the values that we've already found.
     */
    while ( filledUiIndices.nonEmpty || filledVjIndices.nonEmpty ) {
      if (filledUiIndices.nonEmpty) {
        val nextUiIndex = filledUiIndices.dequeue
        for (p <- basicPairs)
          if ( (p._1 == nextUiIndex) && !vjChanged(p._2) )
            setVj(p._2, (linkFlowCosts(p._1)(p._2) - ui(p._1)))
      }
      else {
        val nextVjIndex = filledVjIndices.dequeue
        for (p <- basicPairs)
          if ( (p._2 == nextVjIndex) && !uiChanged(p._1) )
            setUi(p._1, (linkFlowCosts(p._1)(p._2) - vj(p._2)))
      }
    }
  }

  /** Returns the "star pair" that needs to have it's allocation increased
    * for the next tableau.
    *
    * The star pair is found by searching for any pairs that need to have
    * their allocation modified to ensure dual-feasibility of their ui and
    * vj values (ie. any pairs that have the sum of their corresponding ui
    * and vj values exceed the value of the link-flow cost.)
    *
    * @return The star pair for the tableau.
    */
  def starPair(): Tuple2[Int, Int] = 
    indices.find { p => ui(p._1) + vj(p._2) > linkFlowCosts(p._1)(p._2) }.get

}
