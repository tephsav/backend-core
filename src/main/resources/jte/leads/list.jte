@param java.util.List<ru.mentee.power.crm.model.Lead> leads
@param String email
@param String status

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Lead List</h2>

        <div class="bg-white shadow rounded-lg p-4 mb-6">
            <form method="get" action="/leads">
                <label for="email">
                    Email
                    <input type="text" name="email" value="${email}"
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500">
                </label>
                <label for="status">
                    Status
                    <select name="status"
                            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500">
                        <option value="" selected="${status.isEmpty()}">Все</option>
                        <option value="NEW" selected="${status.equals("NEW")}">Новый</option>
                        <option value="CONTACTED" selected="${status.equals("CONTACTED")}">Связались</option>
                        <option value="QUALIFIED" selected="${status.equals("QUALIFIED")}">Квалифицированный</option>
                    </select>
                </label>
                <button type="submit" class="bg-white shadow rounded-lg p-4 mb-6">
                    Фильтр
                </button>
                <a href="/leads"
                   class="bg-white shadow rounded-lg p-4 mb-6">
                    Сброс
                </a>
            </form>
        </div>

        <div>
            <a
                    href="/leads/new"
                    class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
            >
                + Добавить лида
            </a>
        </div>
        <br>

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
                <tr>
                    <th class="px-4 py-2 text-left">Email</th>
                    <th class="px-4 py-2 text-left">Phone</th>
                    <th class="px-4 py-2 text-left">Company</th>
                    <th class="px-4 py-2 text-left">Status</th>
                    <th class="px-4 py-2 text-left"></th>
                    <th class="px-4 py-2 text-left"></th>
                </tr>
            </thead>
            <tbody>
                @for(var lead : leads)
                    <tr class="border-t hover:bg-gray-50">
                        <td class="px-4 py-2">${lead.email()}</td>
                        <td class="px-4 py-2">${lead.phone()}</td>
                        <td class="px-4 py-2">${lead.company()}</td>
                        <td class="px-4 py-2">
                            <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                ${lead.status()}
                            </span>
                        </td>
                        <td>
                            <a
                                    href = "/leads/${String.valueOf(lead.id())}/edit"
                                    class="text-blue-600 hover:underline"
                            >
                                Редактировать
                            </a>
                        </td>
                        <td>
                            <form method="post" action="/leads/${String.valueOf(lead.id())}/delete" class="inline"
                                  onclick="return confirm('Удалить лида ${lead.email()}?')">
                                <button type="submit" class="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700">
                                    Удалить
                                </button>
                            </form>
                        </td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>
`)
