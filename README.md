# Derivatives Calculator
This console-based calculator differentiates mathematical expressions according to single-variable differentiation rules. It contains my implementation of the [Shunting Yard algorithm](https://en.wikipedia.org/wiki/Shunting_yard_algorithm) by Edsger Dijkstra, to parse the user-inputted expression and convert it into a Tree. The Tree is then put through a Differentiator and Simplifier I designed, and the derivative is given to the user. 

## Calculator Rules
The following user input rules apply: 
  - Indicate the variable of differentiation first, by using [Leibniz's Notation](https://en.wikipedia.org/wiki/Leibniz%27s_notation#:~:text=In%20calculus%2C%20Leibniz's%20notation%2C%20named,of%20x%20and%20y%2C%20respectively.)
    - For example, "d/dx (x ^ 2) + 1" or "d/dy log_2(4 * y)".
  - The calculator limits the user to two variables within the expression.
    - The first variable of differentiation is indicated in Leibniz's notation.
    - The second variable is somewhere inside the expression which the calculator will find.
  - Integer and decimal numbers are acceptable for numbers/operands, while Euler's number and pi are unacceptable.
  - The following five operators are acceptable: '+', '-', '*', '/', '^'.
    - Unary operators are unacceptable.
    - Concatenating variables onto numbers, such as "4x" is unacceptable. Instead, type "4 * x" or "(4 * x)".
  - The following mathematical functions are acceptable:
    - Trigonometric functions: "sin", "cos", "tan", "sec", "csc", "cot".
    - Inverse trigonometric functions: "arcsin", "arccos", "arctan", "arcsec", "arccsc", "arccot".
    - Logarithmic functions: "log", "ln"
      - If the user wishes for custom base values, an underscore should follow after the function. For example, "log_2" or "log_10".

If you wish to see examples of acceptable expression formatting, please visit this file: [expressions.txt](https://github.com/jacobklymenko/derivatives-calculator/blob/main/expressions.txt). Please be careful, this file was used for testing the reduction percentage of my Simplifier and does not include Leibniz's notation. However, each expression is differentiated with 'x' as the variable of differentiation.

## Interior Functionality
  1. The user expression is taken as a String and the Leibniz notation is parsed to identify the variable of differentiation.
  2. The rest of the expression String is separated into a List, keeping the infix notation characteristics of the expression.
  3. The List is fed into the Shunting Yard algorithm, producing a Tree representing the expression according to operator precedence and associativity rules.
  4. The Tree is put through the Differentiator, producing a second Tree representing the derivative of the expression.
  5. The second Tree is put through the Simplifier, producing a third Tree representing the most simplified form it can do.
  6. Finally, the calculator converts the second and third Trees into Strings and outputs them both. These two Strings represent the extended and simplified derivative solutions.

## Calculator Statistics
  - **100%** of this program was unit tested, using **[JUnit](https://en.wikipedia.org/wiki/JUnit)**.
  - Through empirical testing, using the 100 expressions in the [expressions.txt](https://github.com/jacobklymenko/derivatives-calculator/blob/main/expressions.txt) file, the Simplifier used in the calculator improves the output derivatives expression readability by an average of **38%**.
    - The test is conducted through the [SimplifierReductionTest.java](https://github.com/jacobklymenko/derivatives-calculator/blob/main/tests/SimplifierReductionTest.java) file.
  - Implemented **12 differentation rules** resulting in a **95% correctness**.
