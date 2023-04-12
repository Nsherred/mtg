package io.nicronomicon.cli

import kotlinx.cli.*
import java.nio.file.Path

@OptIn(ExperimentalCli::class)
class CliArguments(
    args: Array<String>,
    programName: String,
    useDefaultHelpShortName: Boolean = true,
    prefixStyle: OptionPrefixStyle = OptionPrefixStyle.LINUX,
    skipExtraArguments: Boolean = false,
    strictSubcommandOptionsOrder: Boolean = false
) : ArgParser(programName, useDefaultHelpShortName, prefixStyle, skipExtraArguments, strictSubcommandOptionsOrder) {
    init {
        subcommands(
            DeckDownloadCommand(),
            DeckParserCommand(),
            DeckRecommendationCommand(),
            DeckAnalysisCommand()
        )
        parse(args)
    }

}


object FilePath : ArgType<Path>(true) {
    override val description: kotlin.String
        get() = ""

    override fun convert(value: kotlin.String, name: kotlin.String): Path {
        return Path.of(value)
    }

}