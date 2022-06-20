from Polynomial import Polynomial

if __name__ == '__main__':
    polynomial1 = Polynomial({4: 5, 2: 2})
    polynomial2 = Polynomial({3: 1, 1: 5})

    polynomial3 = polynomial1.add(polynomial2)
    print(polynomial3)

    polynomial3 = polynomial1.modulo(3)
    print(polynomial3.coefficients)

    print(polynomial3.is_equal(polynomial1))

    polynomial3 = polynomial1.multiply(polynomial2)
    print(polynomial3)
