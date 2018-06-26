package io.github.lingalone.javafonttools.font;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/26
 */

@Data
public class FontImage {

    private Integer imgSize;
    private Integer fontSize;
    private String fontFile;
    private String saveDir;
    private List<String> fontKeys;

}
