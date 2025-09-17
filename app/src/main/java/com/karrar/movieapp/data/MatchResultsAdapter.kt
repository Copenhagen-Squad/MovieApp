package com.karrar.movieapp.data

// Pure data interface - no UI dependencies
interface MatchResultsDataAdapter {
    fun getItems(): List<MatchItemData>
    fun updateItems(newItems: List<MatchItemData>)
    fun getItem(position: Int): MatchItemData?
    fun getItemCount(): Int
}

// Implementation for data operations
class MatchResultsDataAdapterImpl : MatchResultsDataAdapter {
    private val items = mutableListOf<MatchItemData>()

    override fun getItems(): List<MatchItemData> = items.toList()

    override fun updateItems(newItems: List<MatchItemData>) {
        items.clear()
        items.addAll(newItems)
    }

    override fun getItem(position: Int): MatchItemData? {
        return if (position in 0 until items.size) items[position] else null
    }

    override fun getItemCount(): Int = items.size
}

// Pure data class for match results
data class MatchItemData(
    val id: Int,
    val title: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val genres: String?,
    val voteAverage: Double?,
    val runtimeFormatted: String?,
    val releaseDateShort: String?
)

// Callback interface for actions (still data layer)
interface MatchResultActions {
    fun onPlaySelected(itemId: Int)
    fun onSaveSelected(itemId: Int)
    fun onDetailsSelected(itemId: Int)
}

// Data mapper to convert between different data representations
object MatchDataMapper {
    fun fromDomainToData(domainItem: Any /* your domain model */): MatchItemData {
        // Convert from domain model to data model
        return MatchItemData(
            id = 0, // map from domain
            title = null,
            posterUrl = null,
            backdropUrl = null,
            genres = null,
            voteAverage = null,
            runtimeFormatted = null,
            releaseDateShort = null
        )
    }

    fun fromDataToDomain(dataItem: MatchItemData): Any /* your domain model */ {
        // Convert from data model to domain model
        return Any() // Replace with actual domain model conversion
    }
}