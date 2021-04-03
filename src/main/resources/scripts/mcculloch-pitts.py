def neruonOutput(inputs, weights, bias, activation_function):
    return activation_function(membrane_potential(inputs, weights, bias))


def membrane_potential(inputs, weights, bias):
    sum = 0
    for i in range(0, len(inputs) - 1):
        sum += inputs[i] * weights[i] + bias
    return sum


def activation_function(membrane_potential):
    if membrane_potential >= 0:
        return 1
    return 0


def train_weights(dataset, weights, bias, activation_function, learn_rate, generations):
    results = []
    print("Gen 1:")
    results.append(single_training(dataset, weights, bias, activation_function, learn_rate))
    for i in range(1, generations):
        print("\nGen {}:".format(i + 1))
        results.append(single_training(dataset, results[len(results) - 1], bias, activation_function, learn_rate))
    return results


def single_training(dataset, weights, bias, activation_function, learn_rate):
    n = 1
    correct = 0
    for inputs in dataset:
        print("Input row {}: {}".format(n, inputs))
        n += 1
        prediction = neruonOutput(inputs, weights, bias, activation_function)
        actual = inputs[-1]
        print("Prediction: {}, Actual: {}".format(prediction, actual))
        error = actual - prediction
        print("Before training: {}".format(weights))
        for i in range(len(inputs) - 1):
            weights[i] = weights[i] + learn_rate * error * inputs[i]
        print("After training: {}".format(weights))
        if prediction == actual:
            print('\033[92mCorrect \033[0m')
            correct += 1
        else:
            print('\033[1;31mIncorrect \033[0m')
    print("{}/{} correct".format(correct, n - 1))
    return weights


dataset = [[2.7810836, 2.550537003, 0],
           [1.465489372, 2.362125076, 0],
           [3.396561688, 4.400293529, 0],
           [1.38807019, 1.850220317, 0],
           [3.06407232, 3.005305973, 0],
           [7.627531214, 2.759262235, 1],
           [5.332441248, 2.088626775, 1],
           [6.922596716, 1.77106367, 1],
           [8.675418651, -0.242068655, 1],
           [7.673756466, 3.508563011, 1]]
weights = [-0.1, 0.206536401400000070]
bias = 0.5

train_weights(dataset, weights, bias, activation_function, 0.1, 100)
