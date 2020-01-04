package com.drew.metadata.wav;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/**
 * @author Payton Garland
 */
public class WavDescriptor extends TagDescriptor<WavDirectory>
{
    public WavDescriptor(@NotNull WavDirectory directory)
    {
        super(directory);
    }

    @Override
    @Nullable
    public String getDescription(int tagType)
    {
        return super.getDescription(tagType);
    }
}
