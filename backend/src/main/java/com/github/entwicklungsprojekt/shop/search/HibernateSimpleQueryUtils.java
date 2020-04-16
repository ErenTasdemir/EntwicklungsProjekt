package com.github.entwicklungsprojekt.shop.search;

import lombok.NonNull;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HibernateSimpleQueryUtils {

    private static final Set<Character> TERM_DELIMITER = Set.of('(', ')', '|', ' ', '&', '-');

    private static final Set<Character> SPECIAL_CHARACTERS = Set.of('~', '*');


    static String makeQueryPrefixAndFuzzy(@NonNull String simpleQuery) {
        var trimmedSimpleQuery = simpleQuery.trim();
        var replaceableTermIndexPairs = getReplaceableTermsOfQuery(trimmedSimpleQuery);

        if(replaceableTermIndexPairs.isEmpty()) {
            return trimmedSimpleQuery;
        }

        var fuzzyAndPrefixQuery = trimmedSimpleQuery;
        var addedOffsetByReplacingTerms = 0;

        for(var pair : replaceableTermIndexPairs) {
            var termStartIndex = pair.getFirst() + addedOffsetByReplacingTerms;
            var termEndIndex = pair.getSecond() + addedOffsetByReplacingTerms;
            var termStartEndIndexPair = Pair.of(termStartIndex, termEndIndex);

            var preReplacementQueryLength = fuzzyAndPrefixQuery.length();
            fuzzyAndPrefixQuery = replaceTermWithFuzzyAndPrefixDisjunction(termStartEndIndexPair, fuzzyAndPrefixQuery);
            var postReplacementQueryLength = fuzzyAndPrefixQuery.length();

            addedOffsetByReplacingTerms += postReplacementQueryLength - preReplacementQueryLength;
        }

        return fuzzyAndPrefixQuery;
    }


    private static String replaceTermWithFuzzyAndPrefixDisjunction(Pair<Integer, Integer> startEndIndexPair, String simpleQuery) {
        var termStartIndex = startEndIndexPair.getFirst();
        var termEndIndex = startEndIndexPair.getSecond();

        var term = simpleQuery.substring(termStartIndex, termEndIndex + 1);
        var replaceTerm = String.format("(%s)",
                createLuceneQueryString(List.of(term, term + "~1", term + "*"), "|"));

        return replaceSubstring(startEndIndexPair, simpleQuery, replaceTerm);
    }


    private static String replaceSubstring(Pair<Integer, Integer> startEndIndexPair, String stringToReplaceIn, String replacement) {
        var startIndex = startEndIndexPair.getFirst();
        var endIndex = startEndIndexPair.getSecond();

        var predReplaceSubstring = startIndex <= 1 ? "" : stringToReplaceIn.substring(0, startIndex);
        var succReplaceSubstring = endIndex >= (stringToReplaceIn.length() - 1) ? "" : stringToReplaceIn.substring(endIndex + 1);

        return String.format("%s%s%s", predReplaceSubstring, replacement, succReplaceSubstring);
    }


    private static int getIndexAfterFirstAppearanceOfAny(int startIndex, Set<Character> characters, String searchedString) {
        var queryCharCount = searchedString.length();

        if(startIndex >= queryCharCount) {
            return -1;
        }

        for(var currentIndex = startIndex; currentIndex < queryCharCount; currentIndex++) {
            if(characters.contains(searchedString.charAt(currentIndex)) && (currentIndex + 1 ) != queryCharCount) {
                return currentIndex + 1;
            }
        }

        return -1;
    }


    private static int getIndexAfterFirstAppearanceOf(int startIndex, char character, String searchedString) {
        var queryCharCount = searchedString.length();

        if(startIndex >= queryCharCount) {
            return -1;
        }

        var indexAfterFirstAppearance = searchedString.substring(startIndex).indexOf(character) + startIndex + 1;
        if(indexAfterFirstAppearance > 0 && indexAfterFirstAppearance < queryCharCount) {
            return indexAfterFirstAppearance;
        }

        return -1;
    }


    private static int getIndexOfFirstAppearanceOfAny(int startIndex, Set<Character> characters, String searchedString) {
        var queryCharCount = searchedString.length();

        if(startIndex >= queryCharCount) {
            return -1;
        }

        for(var currentIndex = startIndex; currentIndex < queryCharCount; currentIndex++) {
            if(characters.contains(searchedString.charAt(currentIndex))) {
                return currentIndex;
            }
        }

        return -1;
    }


    private static int getIndexAfterPhraseTerm(int startIndex, String simpleQuery) {
        var queryCharCount = simpleQuery.length();

        if(startIndex >= queryCharCount) {
            return -1;
        }

        var endIndex = getIndexAfterFirstAppearanceOf(startIndex + 1, '"', simpleQuery);

        if(endIndex > 0 && endIndex < queryCharCount && simpleQuery.charAt(endIndex) == '~') {
            endIndex = getIndexAfterFirstAppearanceOfAny(endIndex, TERM_DELIMITER, simpleQuery);
        }

        return endIndex;
    }


    private static List<Pair<Integer, Integer>> getReplaceableTermsOfQuery(String simpleQuery) {
        var termIndexPairs = new ArrayList<Pair<Integer, Integer>>();
        var queryCharCount = simpleQuery.length();

        var index = 0;
        while(index >= 0 && index < queryCharCount) {
            var currentChar = simpleQuery.charAt(index);

            // skip term delimiters
            if(TERM_DELIMITER.contains(currentChar)) {
                index++;
                continue;
            }

            // ignore phrase terms and near operators
            if(currentChar == '"') {
                index = getIndexAfterPhraseTerm(index, simpleQuery);
                continue;
            }

            var nextSpecialCharIndex = getIndexOfFirstAppearanceOfAny(index, SPECIAL_CHARACTERS, simpleQuery);
            var nextTermDelimiterIndex = getIndexOfFirstAppearanceOfAny(index, TERM_DELIMITER, simpleQuery);

            // no term delimiter found after the current term
            if(nextTermDelimiterIndex < 0) {
                nextTermDelimiterIndex = queryCharCount;
            }
            var termEndIndex = nextTermDelimiterIndex - 1;

            // when a term delimiter is present and the term is not a fuzzy or prefix term add
            // the pair -> ignore fuzzy and prefix terms
            var isSimpleTerm = nextSpecialCharIndex < 0 || nextTermDelimiterIndex < nextSpecialCharIndex;
            if(isSimpleTerm) {
                termIndexPairs.add(Pair.of(index, termEndIndex));

                index = nextTermDelimiterIndex + 1;
            } else {
                // skip this term
                index = getIndexAfterFirstAppearanceOfAny(nextSpecialCharIndex, TERM_DELIMITER, simpleQuery);
            }
        }

        return termIndexPairs;
    }


    private static String createLuceneQueryString(Collection<String> values, String operator,
                                                  Function<String, String> function) {
        if(values.isEmpty()) {
            return "";
        }

        var mappedValues = values.stream()
                .map(function)
                .distinct()
                .collect(Collectors.toList());
        var mappedValueArr = mappedValues.toArray(String[]::new);
        var firstValue = mappedValueArr[0];
        var fieldMatchStringBuilder = new StringBuilder(firstValue);

        for(var valueIndex = 1; valueIndex < mappedValueArr.length; valueIndex++) {
            fieldMatchStringBuilder
                    .append(" ")
                    .append(operator)
                    .append(" ")
                    .append(mappedValueArr[valueIndex]);
        }

        return fieldMatchStringBuilder.toString();
    }


    private static String createLuceneQueryString(Collection<String> values, String operator) {
        return createLuceneQueryString(values, operator, Function.identity());
    }

}
