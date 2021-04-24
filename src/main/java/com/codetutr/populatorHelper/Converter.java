package com.codetutr.populatorHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

public interface Converter<SOURCE, TARGET> extends org.springframework.core.convert.converter.Converter<SOURCE, TARGET> {
    TARGET convert(SOURCE var1) throws ConversionException;

    TARGET convert(SOURCE var1, TARGET var2) throws ConversionException;

    default List<TARGET> convertAll(Collection<? extends SOURCE> sources) throws ConversionException {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        } else {
            List<TARGET> result = new ArrayList(sources.size());
            Iterator var4 = sources.iterator();

            while(var4.hasNext()) {
                SOURCE source = (SOURCE)var4.next();
                result.add(this.convert(source));
            }

            return result;
        }
    }

    default List<TARGET> convertAllIgnoreExceptions(Collection<? extends SOURCE> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        } else {
            List<TARGET> targets = new ArrayList(sources.size());
            Iterator var4 = sources.iterator();

            while(var4.hasNext()) {
                Object source = (Object)var4.next();

                try {
                    targets.add(this.convert((SOURCE) source));
                } catch (ConversionException var6) {
                    this.getLogger().warn("Exception while converting object!", var6);
                }
            }

            return targets;
        }
    }

    default Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }
}
