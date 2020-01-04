/*
 * Copyright 2002-2019 Drew Noakes and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * More information about this project is available at:
 *
 *    https://drewnoakes.com/code/exif/
 *    https://github.com/drewnoakes/metadata-extractor
 */

package com.drew.metadata.exif;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.drew.metadata.exif.ExifThumbnailDirectory.*;

/**
 * Provides human-readable string representations of tag values stored in a {@link ExifThumbnailDirectory}.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
@SuppressWarnings("WeakerAccess")
public class ExifThumbnailDescriptor extends ExifDescriptorBase<ExifThumbnailDirectory>
{
    public ExifThumbnailDescriptor(@NotNull ExifThumbnailDirectory directory)
    {
        super(directory);
    }

    @Override
    @Nullable
    public String getDescription(int tagType)
    {
        switch (tagType) {
            case TAG_THUMBNAIL_OFFSET:
                return getThumbnailOffsetDescription();
            case TAG_THUMBNAIL_LENGTH:
                return getThumbnailLengthDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getThumbnailLengthDescription()
    {
        String value = _directory.getString(TAG_THUMBNAIL_LENGTH);
        return value == null ? null : value + " bytes";
    }

    @Nullable
    public String getThumbnailOffsetDescription()
    {
        String value = _directory.getString(TAG_THUMBNAIL_OFFSET);
        return value == null ? null : value + " bytes";
    }
}
