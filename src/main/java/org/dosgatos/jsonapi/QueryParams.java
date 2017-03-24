package org.dosgatos.jsonapi;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class QueryParams {

    private RelatedResources relatedResources;
    private Fieldsets fieldsets;
    private Sorting sorting;
    private Pagination pagination;
    private Filtering filtering;

    @Context
    public void setUriInfo(UriInfo uriInfo) {
        this.relatedResources = RelatedResources.from(uriInfo);
        this.fieldsets = Fieldsets.from(uriInfo);
        this.sorting = Sorting.from(uriInfo);
        this.pagination = Pagination.from(uriInfo);
        this.filtering = Filtering.from(uriInfo);
    }

    public RelatedResources getRelatedResources() {
        return relatedResources;
    }

    public Fieldsets getFieldsets() {
        return fieldsets;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public Filtering getFiltering() {
        return filtering;
    }

    public static class RelatedResources {

        private static final ParameterParser PARSER = ParameterParser.createFor("include");

        private final ImmutableList<String> values;

        public static RelatedResources from(UriInfo uriInfo) {
            return new RelatedResources(PARSER.parse(uriInfo));
        }

        private RelatedResources(ImmutableList<String> values) {
            this.values = values;
        }

        public ImmutableList<String> getValues() {
            return values;
        }
    }

    public static class Fieldsets {

        private static final IndexedParameterParser PARSER = IndexedParameterParser.createFor("fields");

        private final ImmutableMap<String, ImmutableList<String>> keysToValues;

        public static Fieldsets from(UriInfo uriInfo) {
            return new Fieldsets(PARSER.parse(uriInfo));
        }

        private Fieldsets(ImmutableMap<String, ImmutableList<String>> keysToValues) {
            this.keysToValues = keysToValues;
        }

        public ImmutableMap<String, ImmutableList<String>> getKeysToValues() {
            return keysToValues;
        }
    }

    public static class Sorting {

        private static final ParameterParser PARSER = ParameterParser.createFor("sort");

        private final ImmutableList<String> values;

        public static Sorting from(UriInfo uriInfo) {
            return new Sorting(PARSER.parse(uriInfo));
        }

        private Sorting(ImmutableList<String> values) {
            this.values = values;
        }

        public ImmutableList<String> getValues() {
            return values;
        }
    }

    public static class Pagination {

        private static final IndexedParameterParser PARSER = IndexedParameterParser.createFor("page");

        private final ImmutableMap<String, ImmutableList<String>> keysToValues;

        public static Pagination from(UriInfo uriInfo) {
            return new Pagination(PARSER.parse(uriInfo));
        }

        private Pagination(ImmutableMap<String, ImmutableList<String>> keysToValues) {
            this.keysToValues = keysToValues;
        }

        public ImmutableMap<String, ImmutableList<String>> getKeysToValues() {
            return keysToValues;
        }
    }
    
    public static class Filtering {

        private static final IndexedParameterParser PARSER = IndexedParameterParser.createFor("filter");

        private final ImmutableMap<String, ImmutableList<String>> keysToValues;

        public static Filtering from(UriInfo uriInfo) {
            return new Filtering(PARSER.parse(uriInfo));
        }

        private Filtering(ImmutableMap<String, ImmutableList<String>> keysToValues) {
            this.keysToValues = keysToValues;
        }

        public ImmutableMap<String, ImmutableList<String>> getKeysToValues() {
            return keysToValues;
        }
    }

    /**
     * 
     * PARSER FOR PARAMETERS OF PATTERN KEY=VALUE
     *
     */
    protected static class ParameterParser {

        private final String key;
        private final EntryToListMapper entryToListMapper;

        public static ParameterParser createFor(String parameterName) {
            return new ParameterParser(parameterName);
        }

        private ParameterParser(String parameterName) {
            this.key = parameterName;
            this.entryToListMapper = EntryToListMapper.create();
        }

        public ImmutableList<String> parse(UriInfo uriInfo) {
            return uriInfo.getQueryParameters().entrySet().stream()
                          .filter(e -> key.equalsIgnoreCase(e.getKey()))
                          .map(entryToListMapper)
                          .collect(Collector.of(
                              ImmutableList.Builder<String>::new, 
                              (builder, list) -> builder.addAll(list), 
                              (builder1, builder2) -> builder1.addAll(builder2.build()), 
                              ImmutableList.Builder::build));
        }

        protected static class EntryToListMapper implements Function<Entry<String, List<String>>, List<String>> {

            public static EntryToListMapper create() {
                return new EntryToListMapper();
            }

            @Override
            public List<String> apply(Entry<String, List<String>> e) {
                return ImmutableList.copyOf(generateList(e));
            }

            private List<String> generateList(Entry<String, List<String>> e) {
                return e.getValue().stream()
                                   .map(string -> string.split(","))
                                   .flatMap(Arrays::stream)
                                   .distinct()
                                   .collect(Collectors.toList());
            }
        }
    }

    /**
     * 
     * PARSER FOR PARAMETERS OF PATTERN KEY[INDEX]=VALUE
     *
     */
    protected static class IndexedParameterParser {

        private final Pattern keyPattern;
        private final EntryMapper entryMapper;

        public static IndexedParameterParser createFor(String parameterName) {
            return new IndexedParameterParser(parameterName);
        }

        private IndexedParameterParser(String parameterName) {
            this.keyPattern = Pattern.compile(parameterName + "\\[([^\\]]+)\\]");
            this.entryMapper = EntryMapper.createFor(keyPattern);
        }

        public ImmutableMap<String, ImmutableList<String>> parse(UriInfo uriInfo) {
            return uriInfo.getQueryParameters().entrySet().stream()
                          .filter(e -> keyPattern.matcher(e.getKey()).matches())
                          .map(entryMapper)
                          .collect(Collector.of(
                              ImmutableMap.Builder<String, ImmutableList<String>>::new, 
                              (builder, entry) -> builder.put(entry.getKey(), ImmutableList.copyOf(entry.getValue())), 
                              (builder1, builder2) -> builder1.putAll(builder2.build()), 
                              ImmutableMap.Builder::build));
        }

        protected static class EntryMapper implements Function<Entry<String, List<String>>, Entry<String, List<String>>> {

            private final Pattern keyPattern;

            public static EntryMapper createFor(Pattern keyPattern) {
                return new EntryMapper(keyPattern);
            }

            private EntryMapper(Pattern keyPattern) {
                this.keyPattern = keyPattern;
            }

            @Override
            public Entry<String, List<String>> apply(Entry<String, List<String>> e) {
                return Maps.immutableEntry(generateKey(e), generateValue(e));
            }

            private String generateKey(Entry<String, List<String>> e) {
                return Optional.of(e.getKey())
                               .map(keyPattern::matcher)
                               .filter(Matcher::matches)
                               .map(matcher -> matcher.group(1))
                               .orElse("");
            }

            private List<String> generateValue(Entry<String, List<String>> e) {
                return e.getValue().stream()
                                   .map(string -> string.split(","))
                                   .flatMap(Arrays::stream)
                                   .distinct()
                                   .collect(Collectors.toList());
            }
        }
    }
}
