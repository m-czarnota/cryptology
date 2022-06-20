class Polynomial:
    def __init__(self, coefficients = {}):
        self.coefficients = coefficients

    def add(self, polynomial):
        coefficients = {}

        for d in [self.coefficients, polynomial.coefficients]:
            for key, value in d.items():
                if key not in coefficients.keys():
                    coefficients[key] = value
                else:
                    coefficients[key] += value

        return Polynomial(coefficients)

    def modulo(self, p):
        coefficients = {}

        for key, value in self.coefficients.items():
            coefficients[key] = value % p

        return Polynomial(coefficients)

    def multiply(self, polynomial):
        coefficients = {}

        for key1, value1 in self.coefficients.items():
            for key2, value2 in polynomial.coefficients.items():
                result_key = key1 + key2
                result_value = value1 * value2

                if result_key not in coefficients.keys():
                    coefficients[result_key] = result_value
                else:
                    coefficients[result_key] += result_value

        return Polynomial(coefficients)
                

    def is_equal(self, polynomial):
        return polynomial.__str__() == self.__str__()

    def __str__(self):
        coefficients = sorted(self.coefficients.items())
        output = ''

        for key, value in reversed(coefficients):
            output += ' + ' if value >= 0 else '-'
            output += str(value) + 'x' if value != 1 and value != -1 else 'x'

            if key != 1:
                output += '**' + str(key)

        return output
