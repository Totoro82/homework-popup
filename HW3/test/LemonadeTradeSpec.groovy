import spock.lang.Specification
import spock.lang.Unroll

class LemonadeTradeSpec extends Specification {

    double solve(List<List> trades) {
        Trade[] arr = trades.collect { new Trade(it[0], it[1], it[2] as double) } as Trade[]
        return new TradeSolver(arr.length, arr).solve()
    }

    String runProgram(String input) {
        def proc = ['java', '-cp', 'src', 'LemonadeTrade'].execute()
        proc.outputStream.write(input.bytes)
        proc.outputStream.close()
        return proc.inputStream.text.trim()
    }

    // --- Basic cases ---

    def "no children means 0 blue"() {
        expect:
        solve([]) == 0.0
    }

    def "direct pink to blue"() {
        expect:
        Math.abs(solve([["blue", "pink", 1.5]]) - 1.5) < 1e-6
    }

    def "no path to blue returns 0"() {
        expect:
        solve([["green", "pink", 1.5], ["red", "green", 1.2]]) == 0.0
    }

    // --- Chain cases ---

    def "two-step chain pink -> green -> blue"() {
        expect:
        Math.abs(solve([
            ["green", "pink", 1.5],
            ["blue", "green", 1.2]
        ]) - 1.8) < 1e-6
    }

    def "three-step chain pink -> green -> red -> blue"() {
        expect:
        Math.abs(solve([
            ["green", "pink", 1.5],
            ["red", "green", 1.3],
            ["blue", "red", 1.1]
        ]) - 1.5 * 1.3 * 1.1) < 1e-6
    }

    // --- Choosing best path ---

    def "picks better of two paths to blue"() {
        given: "path A: pink->green->blue = 1.5*1.5=2.25, path B: pink->blue = 1.8"
        expect:
        Math.abs(solve([
            ["green", "pink", 1.5],
            ["blue", "pink", 1.8],
            ["blue", "green", 1.5]
        ]) - 2.25) < 1e-6
    }

    def "picks direct path when chain is worse"() {
        given: "chain: 0.6*0.7=0.42, direct: 0.9"
        expect:
        Math.abs(solve([
            ["green", "pink", 0.6],
            ["blue", "pink", 0.9],
            ["blue", "green", 0.7]
        ]) - 0.9) < 1e-6
    }

    // --- Cycles that amplify ---

    def "cycle amplifies value before converting to blue"() {
        expect:
        Math.abs(solve([
            ["green", "pink", 1.9],
            ["pink", "green", 1.9],
            ["blue", "pink", 1.0]
        ]) - 1.9 * 1.9) < 1e-6
    }

    def "multiple cycles amplify beyond 10 -> capped at 10"() {
        expect:
        solve([
            ["green", "pink", 1.9],
            ["pink", "green", 1.9],
            ["green", "pink", 1.9],
            ["pink", "green", 1.9],
            ["blue", "pink", 1.0]
        ]) == 10.0
    }

    // --- Cap at 10 ---

    def "result above 10 is capped at 10"() {
        expect:
        solve([["blue", "pink", 1.9],
               ["pink", "blue", 1.9],
               ["blue", "pink", 1.9],
               ["pink", "blue", 1.9],
               ["blue", "pink", 1.9]]) == 10.0
    }

    // --- Edge cases ---

    def "trade with type you dont have does nothing"() {
        expect:
        solve([["blue", "green", 1.5]]) == 0.0
    }

    def "self-trade amplifies"() {
        expect:
        Math.abs(solve([
            ["pink", "pink", 1.5],
            ["blue", "pink", 1.0]
        ]) - 1.5) < 1e-6
    }

    def "trade order matters - cant go back"() {
        given: "blue->green trade comes BEFORE we have green"
        expect:
        solve([
            ["blue", "green", 1.5],
            ["green", "pink", 1.2]
        ]) == 0.0
    }

    // --- Full I/O test via main ---

    def "full program n=0 outputs formatted zero"() {
        expect:
        runProgram("0\n").toDouble() == 0.0
    }

    def "full program basic chain"() {
        expect:
        Math.abs(runProgram("2\ngreen pink 1.5\nblue green 1.2\n").toDouble() - 1.8) < 1e-6
    }

    // --- Larger random-ish cases ---

    def "long chain of 10 conversions"() {
        given:
        def types = ["pink", "a", "b", "c", "d", "e", "f", "g", "h", "blue"]
        def trades = []
        for (int i = 0; i < types.size() - 1; i++) {
            trades << [types[i + 1], types[i], 1.1]
        }

        expect:
        Math.abs(solve(trades) - Math.pow(1.1, 9)) < 1e-6
    }

    @Unroll
    def "rate=#rate direct pink->blue gives #expected"() {
        expect:
        Math.abs(solve([["blue", "pink", rate]]) - expected) < 1e-6

        where:
        rate | expected
        0.51 | 0.51
        1.0  | 1.0
        1.99 | 1.99
    }

    // --- Trade blue AWAY and get it back (blue as intermediate) ---

    def "trade blue away then get more back via cycle"() {
        given: "pink->blue(1.5) -> blue->green(1.9) -> green->blue(1.9) = 1.5*1.9*1.9=5.415"
        expect:
        Math.abs(solve([
            ["blue", "pink", 1.5],
            ["green", "blue", 1.9],
            ["blue", "green", 1.9]
        ]) - 1.5 * 1.9 * 1.9) < 1e-6
    }

    // --- Diamond: two independent paths to blue, DP must NOT sum them ---

    def "diamond shape picks best single path not sum"() {
        given: "pink->green->blue = 1.5*1.0=1.5, pink->red->blue = 1.3*1.0=1.3"
        expect:
        Math.abs(solve([
            ["green", "pink", 1.5],
            ["red", "pink", 1.3],
            ["blue", "green", 1.0],
            ["blue", "red", 1.0]
        ]) - 1.5) < 1e-6
    }

    // --- Noise trades that don't connect to anything useful ---

    def "many irrelevant trades dont affect result"() {
        expect:
        Math.abs(solve([
            ["x1", "x2", 1.5],
            ["x3", "x4", 1.9],
            ["green", "pink", 1.5],
            ["x5", "x6", 0.8],
            ["blue", "green", 1.2],
            ["x7", "x8", 1.1],
        ]) - 1.8) < 1e-6
    }

    // --- Long chain (N=1000) to test precision ---

    def "long chain N=1000 with rate 1.001 stays precise"() {
        given:
        def trades = []
        def types = (0..1000).collect { i -> i == 0 ? "pink" : (i == 1000 ? "blue" : "t${i}") }
        for (int i = 0; i < 1000; i++) {
            trades << [types[i + 1], types[i], 1.001]
        }
        double expected = Math.min(10.0, Math.pow(1.001, 1000))

        expect:
        Math.abs(solve(trades) - expected) < 1e-6
    }

    // --- Long chain with rate < 1 shrinks to near zero ---

    def "long chain with bad rates gives near zero"() {
        given:
        def trades = []
        def types = (0..100).collect { i -> i == 0 ? "pink" : (i == 100 ? "blue" : "t${i}") }
        for (int i = 0; i < 100; i++) {
            trades << [types[i + 1], types[i], 0.51]
        }

        expect:
        solve(trades) < 1e-6
    }

    // --- Multiple children offer same trade, pick best ---

    def "multiple pink->blue trades picks the best rate"() {
        expect:
        Math.abs(solve([
            ["blue", "pink", 0.8],
            ["blue", "pink", 1.2],
            ["blue", "pink", 0.9],
        ]) - 1.2) < 1e-6
    }

    // --- Long type names (10 chars max) ---

    def "works with long type names"() {
        expect:
        Math.abs(solve([
            ["abcdefghij", "pink", 1.5],
            ["blue", "abcdefghij", 1.3]
        ]) - 1.95) < 1e-6
    }

    // --- Only last trade creates path ---

    def "path only completes on very last trade"() {
        given:
        def trades = []
        for (int i = 0; i < 50; i++) {
            trades << ["junk${i}", "junk${i + 1}", 1.5]
        }
        trades << ["green", "pink", 1.5]
        trades << ["blue", "green", 1.3]

        expect:
        Math.abs(solve(trades) - 1.95) < 1e-6
    }

    // --- Cycle: pink -> A -> pink -> A -> ... -> blue, many cycles ---

    def "many amplifying cycles then convert to blue"() {
        given:
        def trades = []
        for (int i = 0; i < 10; i++) {
            trades << ["green", "pink", 1.5]
            trades << ["pink", "green", 1.5]
        }
        trades << ["blue", "pink", 1.0]
        double expected = Math.min(10.0, Math.pow(1.5, 20))

        expect:
        solve(trades) == 10.0
    }

    // --- Cap boundary: result is just barely over 10 ---

    def "result barely over 10 is capped"() {
        given: "1.9^4 = 13.0321 > 10"
        expect:
        solve([
            ["green", "pink", 1.9],
            ["pink", "green", 1.9],
            ["green", "pink", 1.9],
            ["blue", "green", 1.9]
        ]) == 10.0
    }

    // --- Cap boundary: result is just under 10 ---

    def "result just under 10 is NOT capped"() {
        given: "1.9^3 = 6.859"
        def result = solve([
            ["green", "pink", 1.9],
            ["pink", "green", 1.9],
            ["blue", "pink", 1.9]
        ])

        expect:
        Math.abs(result - 6.859) < 1e-6
        result < 10.0
    }

    // --- Competing chains with shared intermediate ---

    def "shared intermediate type picks best sub-chain"() {
        given: "pink->green(1.2) then green->blue(1.5)=1.8 vs pink->green(1.8) later but green->blue already passed"
        expect:
        Math.abs(solve([
            ["green", "pink", 1.2],
            ["blue", "green", 1.5],
            ["green", "pink", 1.8],
        ]) - 1.8) < 1e-6
    }

    // --- Rate with 7 decimal digits ---

    def "rate with max decimal precision"() {
        expect:
        Math.abs(solve([["blue", "pink", 1.1234567]]) - 1.1234567) < 1e-6
    }

    // --- All trades want blue (trade blue away for profit?) ---

    def "trading blue away is only good if you get more back"() {
        given: "pink->blue(0.8), blue->green(1.9), green->blue(1.9) = 0.8*1.9*1.9=2.888"
        def result = solve([
            ["blue", "pink", 0.8],
            ["green", "blue", 1.9],
            ["blue", "green", 1.9],
        ])

        expect:
        Math.abs(result - 0.8 * 1.9 * 1.9) < 1e-6
    }

    // --- Single trade that does nothing useful ---

    def "single trade green->red with no green available"() {
        expect:
        solve([["red", "green", 1.5]]) == 0.0
    }
}
