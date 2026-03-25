package org.banana.translator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
public class AstList implements AstNode {
    private final List<AstNode> children = new ArrayList<>();

    public void addChild(AstNode node) {
        children.add(node);
    }

    @Override
    public String reconstruct() {
        String content = children.stream()
                .map(AstNode::reconstruct)
                .collect(Collectors.joining(" "));
        return "(" + content + ")";
    }

    @Override
    public String toString() {
        return "List(size=" + children.size() + ")";
    }
}